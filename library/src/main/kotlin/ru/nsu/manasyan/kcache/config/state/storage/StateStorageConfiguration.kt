package ru.nsu.manasyan.kcache.config.state.storage

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.nsu.manasyan.kcache.core.state.storage.StateStorage
import ru.nsu.manasyan.kcache.core.state.storage.ram.RamStateStorageManager
import ru.nsu.manasyan.kcache.core.state.storage.StateStorageManager
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Configuration rules for [StateStorage] beans
 */
@Configuration
@Import(
    value = [
        RedisStateStorageManagerConfiguration::class,
        HazelcastStateStorageManagerConfiguration::class
    ]
)
class StateStorageConfiguration {
    private val logger by LoggerProperty()

    /**
     * Creates [RamStateStorageManager] bean if
     * there was no [RamStateStorageManager] in context or kcache.state-holder
     * property's value in properties file is [KCacheProperties.StateHolder.RAM]
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-storage.name"],
        havingValue = "ram",
        matchIfMissing = true
    )
    fun ramStateHolderManager(): StateStorageManager {
        logger.debug("Building RamStateHolder")
        return RamStateStorageManager()
    }
}