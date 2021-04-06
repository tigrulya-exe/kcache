package ru.nsu.manasyan.kcache.core.annotations

import ru.nsu.manasyan.kcache.core.resultbuilder.KCacheResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.ResponseEntityKCacheResultBuilder
import kotlin.reflect.KClass

// TODO: add description from aspect
/**
 * Enables HTTP-caching of request which was processing by current method
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.CLASS
)
annotation class KCacheable(
    /**
     * Tables on which the return value of the HTTP-request handler method depends
     */
    val tables: Array<String> = [],

    val resultBuilder: KClass<out KCacheResultBuilder<out Any>> =
        ResponseEntityKCacheResultBuilder::class,

    // TODO: add description
    val key: String = ""
)