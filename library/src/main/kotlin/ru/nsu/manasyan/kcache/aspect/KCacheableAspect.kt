package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowire
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import ru.nsu.manasyan.kcache.core.annotations.KCacheableJpa
import ru.nsu.manasyan.kcache.core.etag.builder.ETagBuilder
import ru.nsu.manasyan.kcache.core.etag.extractor.ETagExtractor
import ru.nsu.manasyan.kcache.core.resultbuilder.KCacheResultBuilder
import ru.nsu.manasyan.kcache.core.state.keyparser.KeyParser
import ru.nsu.manasyan.kcache.core.state.storage.StateStorage
import ru.nsu.manasyan.kcache.util.LoggerProperty
import ru.nsu.manasyan.kcache.util.ifDebug
import kotlin.reflect.full.createInstance

@Aspect
@Configurable(autowire = Autowire.BY_TYPE)
open class KCacheableAspect {
    // field autowiring instead of constructor injection used because of
    // plain Aspectj post-compile/load-time weaving support
    @Autowired
    private lateinit var eTagBuilder: ETagBuilder

    @Autowired
    private lateinit var headerExtractor: ETagExtractor

    @Autowired
    private lateinit var keyParser: KeyParser

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
    @Around("execution(* *(..)) && @annotation(kCacheable)")
    open fun wrapKCacheableMethod(
        joinPoint: ProceedingJoinPoint,
        kCacheable: KCacheable,
    ): Any? = handleKCacheable(
        joinPoint,
        kCacheable.tables.toList(),
        kCacheable.key,
        kCacheable.resultBuilder.createInstance()
    )

    @Around("execution(* *(..)) && @annotation(kCacheableJpa)")
    open fun wrapKCacheableJpaMethod(
        joinPoint: ProceedingJoinPoint,
        kCacheableJpa: KCacheableJpa,
    ): Any? = handleKCacheable(
        joinPoint,
        kCacheableJpa.entities.map { it.qualifiedName!! },
        StateStorage.WHOLE_TABLE_KEY,
        kCacheableJpa.resultBuilder.createInstance()
    )

    private fun handleKCacheable(
        joinPoint: ProceedingJoinPoint,
        tables: List<String>,
        key: String,
        resultBuilder: KCacheResultBuilder<*>
    ): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val methodArgs = joinPoint.args

        val currentETag = eTagBuilder.buildETag(
            tables,
            keyParser.parse(key, methodArgs)
        )

        val previousETag = headerExtractor.extract(
            methodSignature.method,
            methodArgs
        )

        if (currentETag == previousETag) {
            logger.ifDebug(
                "Equal ETags for method ${methodSignature.getMethodName()}: " +
                        "$currentETag, returning 304"
            )
            return resultBuilder.onCacheHit(currentETag)
        }

        logger.ifDebug(
            "Different ETags for method ${methodSignature.getMethodName()}: " +
                    "Current [$currentETag] Previous[$previousETag]. Invoking method."
        )

        return resultBuilder.onCacheMiss(
            joinPoint.proceed(),
            currentETag
        )
    }
}

