package ru.nsu.manasyan.kcache.configs

import org.springframework.beans.factory.config.BeanPostProcessor
import ru.nsu.manasyan.kcache.core.RequestStatesMapper
import ru.nsu.manasyan.kcache.core.StateHolder
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * BeanPostProcessor, which injects initial states of all tables from [RequestStatesMapper] to [StateHolder].
 */
class InjectStatesBeanPostProcessor(
    private val mapper: RequestStatesMapper
) : BeanPostProcessor {
    override fun postProcessAfterInitialization(stateHolder: Any, beanName: String): Any? {
        if (stateHolder !is StateHolder) {
            return stateHolder
        }
        mapper.getAllStates()
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