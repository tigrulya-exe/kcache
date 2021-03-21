package ru.nsu.manasyan.kcache.aspect.strategy

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import ru.nsu.manasyan.kcache.core.resultbuilder.hit.KCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.KCacheMissResultBuilder
import kotlin.reflect.full.createInstance

class ReflectionMetadataStrategy : KCacheableAspectStrategy {
    private lateinit var kCacheable: KCacheable

    override var methodSignature: MethodSignature? = null
        set(value) {
            field = value
            kCacheable = value
                ?.method
                ?.getAnnotation(KCacheable::class.java)!!
        }

    override fun getTableStates(): List<String> = kCacheable.tables.toList()

    override fun getOnCacheHitResultBuilder(): KCacheHitResultBuilder<*> =
        kCacheable.onCacheHitResultBuilder.createInstance()

    override fun getOnCacheMissResultBuilder(): KCacheMissResultBuilder<*> =
        kCacheable.onCacheMissResultBuilder.createInstance()
}