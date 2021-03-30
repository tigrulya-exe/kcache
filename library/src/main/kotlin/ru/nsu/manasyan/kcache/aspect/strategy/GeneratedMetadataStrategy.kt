package ru.nsu.manasyan.kcache.aspect.strategy

import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.aspect.getMetadata
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadata
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadataContainer
import kotlin.reflect.full.createInstance

class GeneratedMetadataStrategy(
    private val requestHandlerMetadataContainer: RequestHandlerMetadataContainer
) : KCacheableAspectStrategy {

    private lateinit var metadata: RequestHandlerMetadata

    override var methodSignature: MethodSignature? = null
        set(value) {
            field = value
            metadata = requestHandlerMetadataContainer.getMetadata(value!!)
        }

    override fun getTableStates() = runIfInitialized {
        metadata.tableStates
            .ifEmpty {
                throw IllegalArgumentException("KCacheable annotation should contain at list 1 table")
            }
    }

    // TODO: mb refactor (store class objects in BuilderLocator (map <className, object>)) or add cache
    override fun getResultBuilderFactory() = runIfInitialized {
        metadata.resultBuilderFactory
            .createInstance()
    }

    override fun getKeyExpression() = runIfInitialized {
        metadata.key
    }
}