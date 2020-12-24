package ru.nsu.manasyan.kcache.configs

import com.google.gson.Gson
import org.springframework.beans.factory.config.BeanPostProcessor
import ru.nsu.manasyan.kcache.core.RequestStatesMapper
import ru.nsu.manasyan.kcache.core.StateHolder
import ru.nsu.manasyan.kcache.defaults.RamRequestStatesMapper
import ru.nsu.manasyan.kcache.util.LoggerProperty
import java.time.Instant
import java.time.format.DateTimeFormatter

class InjectStatesBeanPostProcessor : BeanPostProcessor {
    private val logger by LoggerProperty()

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        if (bean !is StateHolder) {
            return bean
        }

        val mappingsFile = this::class.java.classLoader.getResource(
            RequestStatesMapper.MAPPINGS_FILE_PATH
        )
        val mappings = mappingsFile?.let {
            Gson().fromJson(it.readText(), RamRequestStatesMapper::class.java)
        } ?: return bean

        mappings.getAllStates().values
            .flatten()
            .distinct()
            .forEach {
                val newState = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                bean.setState(it, newState)
            }

        logger.debug("Injecting to $beanName")
        return bean
    }
}