package ru.nsu.manasyan.kcache.aspect.strategy

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
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

    override fun getTableStates(): List<String> = runIfInitialized {
        kCacheable.tables.toList()
    }

    override fun getResultBuilderFactory() = runIfInitialized {
        kCacheable.resultBuilderFactory.createInstance()
    }
}