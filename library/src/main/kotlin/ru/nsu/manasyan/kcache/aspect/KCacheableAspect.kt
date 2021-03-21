package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import ru.nsu.manasyan.kcache.aspect.strategy.KCacheableAspectStrategy
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import ru.nsu.manasyan.kcache.core.etag.builder.ETagBuilder
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.util.LoggerProperty
import ru.nsu.manasyan.kcache.util.ifDebug

@Aspect
class KCacheableAspect(
    private val eTagBuilder: ETagBuilder,
    private val headerExtractor: IfNoneMatchHeaderExtractor,
    private val strategy: KCacheableAspectStrategy
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
        strategy.methodSignature = methodSignature

        val currentETag = eTagBuilder.buildETag(
            strategy.getTableStates()
        )
        val previousETag = headerExtractor.extract(
            methodSignature.method,
            joinPoint.args
        )

        val factory = strategy.getResultBuilderFactory()
        if (currentETag == previousETag) {
            logger.ifDebug(
                "Equal ETags for method ${methodSignature.getMethodName()}: " +
                        "$currentETag, returning 304"
            )
            return factory.getOnHitResultBuilder()
                .build(currentETag)
        }

        logger.ifDebug(
            "Different ETags for method ${methodSignature.getMethodName()}: " +
                    "Current [$currentETag] Previous[$previousETag]. Invoking method."
        )

        return factory.getOnMissResultBuilder()
            .build(joinPoint.proceed(), currentETag)
    }
}