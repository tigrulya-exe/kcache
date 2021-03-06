package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import ru.nsu.manasyan.kcache.core.etag.builder.ETagBuilder
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.core.handler.RequestStatesMappings
import ru.nsu.manasyan.kcache.util.EtagResponseBuilder
import ru.nsu.manasyan.kcache.util.LoggerProperty

@Aspect
class KCacheableAspect(
    private val eTagBuilder: ETagBuilder,
    private val headerExtractor: IfNoneMatchHeaderExtractor,
    private val requestStatesMappings: RequestStatesMappings
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
    fun wrapKCacheableControllerMethod(joinPoint: ProceedingJoinPoint): ResponseEntity<*> {
        val methodSignature = joinPoint.signature as MethodSignature
        val currentETag = eTagBuilder.buildETag(
            requestStatesMappings.getRequestStates(methodSignature)
        )
        val previousETag = headerExtractor.extract(
            methodSignature.method,
            joinPoint.args
        )

        val methodName = methodSignature.getMethodName()
        if (currentETag == previousETag) {
            logger.debug("Equal ETags for method ${methodName}: $currentETag, returning 304")
            return EtagResponseBuilder.notModified(currentETag)
        }

        logger.debug(
            "Different ETags for method ${methodName}: Current [$currentETag] " +
                    "Previous[$previousETag]. Invoking method."
        )

        return wrapInResponseEntity(
            joinPoint.proceed()
        ).withEtag(currentETag)
    }
}