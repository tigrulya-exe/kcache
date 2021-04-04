package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.expression.EvaluationContext
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestHeader
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import ru.nsu.manasyan.kcache.core.annotations.KCacheableJpa
import ru.nsu.manasyan.kcache.core.etag.builder.ETagBuilder
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.core.resultbuilder.ResultBuilderFactory
import ru.nsu.manasyan.kcache.util.LoggerProperty
import ru.nsu.manasyan.kcache.util.ifDebug
import kotlin.reflect.full.createInstance

@Aspect
open class KCacheableAspect(
    private val eTagBuilder: ETagBuilder,
    private val headerExtractor: IfNoneMatchHeaderExtractor,
    private val expressionParser: ExpressionParser
) {
    private companion object {
        private const val SPEL_PREFIX = "#"
        private const val SPEL_CONTEXT_ARGS_KEY = "args"
    }

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
    @Around("@annotation(kCacheable)")
    open fun wrapKCacheableMethod(
        joinPoint: ProceedingJoinPoint,
        kCacheable: KCacheable,
    ): Any? = handle(
        joinPoint,
        kCacheable.tables.toList(),
        kCacheable.key,
        kCacheable.resultBuilderFactory.createInstance()
    )

    @Around("@annotation(kCacheableJpa)")
    open fun wrapKCacheableJpaMethod(
        joinPoint: ProceedingJoinPoint,
        kCacheableJpa: KCacheableJpa,
    ): Any? = handle(
        joinPoint,
        kCacheableJpa.entities.map { it.toString() },
        "",
        kCacheableJpa.resultBuilderFactory.createInstance()
    )

    private fun handle(
        joinPoint: ProceedingJoinPoint,
        tables: List<String>,
        key: String,
        resultBuilderFactory: ResultBuilderFactory
    ): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val methodArgs = joinPoint.args

        val currentETag = eTagBuilder.buildETag(
            tables,
            getKey(key, methodArgs)
        )

        val previousETag = headerExtractor.extract(
            methodSignature.method,
            joinPoint.args
        )

        if (currentETag == previousETag) {
            logger.ifDebug(
                "Equal ETags for method ${methodSignature.getMethodName()}: " +
                        "$currentETag, returning 304"
            )
            return resultBuilderFactory.getOnHitResultBuilder().build(currentETag)
        }

        logger.ifDebug(
            "Different ETags for method ${methodSignature.getMethodName()}: " +
                    "Current [$currentETag] Previous[$previousETag]. Invoking method."
        )

        return resultBuilderFactory.getOnMissResultBuilder().build(
            joinPoint.proceed(),
            currentETag
        )
    }

    private fun getKey(keyExpression: String, args: Array<Any>): String {
        if (!keyExpression.startsWith(SPEL_PREFIX)) {
            return keyExpression
        }
        val context: EvaluationContext = StandardEvaluationContext().apply {
            setVariable(SPEL_CONTEXT_ARGS_KEY, args)
        }
        return expressionParser
            .parseExpression(keyExpression)
            .getValue(context)
            ?.toString()
            ?: throw IllegalArgumentException("Key should not be null")
    }
}

