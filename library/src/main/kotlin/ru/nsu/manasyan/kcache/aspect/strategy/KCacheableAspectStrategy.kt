package ru.nsu.manasyan.kcache.aspect.strategy

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.resultbuilder.hit.KCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.KCacheMissResultBuilder

interface KCacheableAspectStrategy {
    var methodSignature: MethodSignature?

    fun getTableStates(): List<String>?
    fun getOnCacheHitResultBuilder(): KCacheHitResultBuilder<*>?
    fun getOnCacheMissResultBuilder(): KCacheMissResultBuilder<*>?
}