package ru.nsu.manasyan.kcache.config

import org.springframework.beans.factory.config.BeanPostProcessor
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadata
import ru.nsu.manasyan.kcache.core.handler.RequestHandlerMetadataContainer
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider

/**
 * BeanPostProcessor, which injects initial states of all table states from [RequestHandlerMetadataContainer] to [StateHolder].
 */
class InjectRequestStatesMappingsBeanPostProcessor(
    private val metadataContainer: RequestHandlerMetadataContainer,
    private val newStateProvider: NewStateProvider
) : BeanPostProcessor {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean !is StateHolder) {
            return bean
        }
        metadataContainer.getAllMetadata()
            .values
            .map { it.tableStates }
            .flatten()
            .distinct()
            .forEach { tableName ->
                bean.setState(
                    tableName,
                    newStateProvider.provide(tableName)
                )
            }

        return bean
    }
}