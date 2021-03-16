package ru.nsu.manasyan.kcache.config

import org.springframework.beans.factory.config.BeanPostProcessor
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.core.state.requestmapping.RequestStatesMappings

/**
 * BeanPostProcessor, which injects initial states of all table states from [RequestStatesMappings] to [StateHolder].
 */
class InjectRequestStatesMappingsBeanPostProcessor(
    private val mappings: RequestStatesMappings,
    private val newStateProvider: NewStateProvider
) : BeanPostProcessor {
    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        if (bean !is StateHolder) {
            return bean
        }
        mappings.getAllStates()
            .values
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