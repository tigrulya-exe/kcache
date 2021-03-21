package ru.nsu.manasyan.kcache.aspect.strategy

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.aspect.getMetadata
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadata
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadataContainer
import kotlin.reflect.full.createInstance

class GeneratedMetadataStrategy(
    private val requestHandlerMetadataContainer: RequestHandlerMetadataContainer
) : KCacheableAspectStrategy {

    private var metadata: RequestHandlerMetadata? = null

    override var methodSignature: MethodSignature? = null
        set(value) {
            field = value
            metadata = requestHandlerMetadataContainer.getMetadata(value!!)
        }

    override fun getTableStates() = metadata
        ?.tableStates
        ?.ifEmpty {
            throw IllegalArgumentException("KCacheable annotation should contain at list 1 table")
        }

    // TODO: mb refactor (store class objects in BuilderLocator (map <className, object>)) or add cache
    override fun getOnCacheHitResultBuilder() = metadata
        ?.onCacheHitResultBuilder
        ?.createInstance()

    override fun getOnCacheMissResultBuilder() = metadata
        ?.onCacheMissResultBuilder
        ?.createInstance()
}