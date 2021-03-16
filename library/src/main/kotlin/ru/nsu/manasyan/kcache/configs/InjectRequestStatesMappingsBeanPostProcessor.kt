package ru.nsu.manasyan.kcache.configs

import org.springframework.beans.factory.config.BeanPostProcessor
import ru.nsu.manasyan.kcache.core.RequestStatesMappings
import ru.nsu.manasyan.kcache.core.StateHolder
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * BeanPostProcessor, which injects initial states of all table states from [RequestStatesMappings] to [StateHolder].
 */
class InjectRequestStatesMappingsBeanPostProcessor(
    private val mappings: RequestStatesMappings
) : BeanPostProcessor {
    override fun postProcessAfterInitialization(stateHolder: Any, beanName: String): Any? {
        if (stateHolder !is StateHolder) {
            return stateHolder
        }
        mappings.getAllStates()
            .values
            .flatten()
            .distinct()
            .forEach {
                stateHolder.setState(
                    it,
                    DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                )
            }

        return stateHolder
    }
}