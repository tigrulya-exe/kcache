package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import ru.nsu.manasyan.kcache.core.etag.builder.ETagBuilder
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadata
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadataContainer
import ru.nsu.manasyan.kcache.util.LoggerProperty
import kotlin.reflect.full.createInstance

@Aspect
class KCacheableAspect(
    private val eTagBuilder: ETagBuilder,
    private val headerExtractor: IfNoneMatchHeaderExtractor,
    private val requestHandlerMetadataContainer: RequestHandlerMetadataContainer
) {
    private val logger by LoggerProperty()

    /**
     * Wraps methods, which were annotated with [KCacheable].
     * If the value of the If-None-Match HTTP header is present in the method arguments
     * and its value matches the value of the calculated ETag, returns the [ResponseEntity] with status code 304
     * and empty body. Meanwhile, the wrapped method is not called.
     *
     * If the HTTP header value If-None-Match is not found in the method arguments,
     * i.e. the argument marked with the [RequestHeader] annotation with the field value [RequestHeader.name]
     * equal to [HttpHeaders.IF_NONE_MATCH], or this argument does not match the current ETag,
     * then the wrapped method is called and its result ([ResponseEntity]) is returned
     * with the ETag header set to the current ETag value.
     */
    @Around("@annotation(ru.nsu.manasyan.kcache.core.annotations.KCacheable)")
    fun wrapKCacheableControllerMethod(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val currentMetadata = requestHandlerMetadataContainer.getMetadata(methodSignature)

        val currentETag = eTagBuilder.buildETag(
            getTableStates(currentMetadata)
        )
        val previousETag = headerExtractor.extract(
            methodSignature.method,
            joinPoint.args
        )

        if (currentETag == previousETag) {
            logger.atDebug()
                .addArgument(methodSignature.getMethodName())
                .log("Equal ETags for method {}: $currentETag, returning 304")
            return getOnCacheHitResultBuilder(currentMetadata)
                .build(currentETag)
        }

        logger.atDebug()
            .addArgument(methodSignature.getMethodName())
            .log(
                "Different ETags for method {}: Current [$currentETag] Previous[$previousETag]. Invoking method."
            )

        return getOnCacheMissResultBuilder(currentMetadata)
            .build(joinPoint.proceed(), currentETag)
    }

    private fun getTableStates(metadata: RequestHandlerMetadata) =
        metadata.tableStates.ifEmpty {
            throw IllegalArgumentException("KCacheable annotation should contain at list 1 table")
        }

    // TODO: mb refactor (store class objects in BuilderLocator (map <className, object>)) or add cache
    private fun getOnCacheHitResultBuilder(metadata: RequestHandlerMetadata) = metadata
        .onCacheHitResultBuilder
        .createInstance()

    private fun getOnCacheMissResultBuilder(metadata: RequestHandlerMetadata) = metadata
        .onCacheMissResultBuilder
        .createInstance()
}