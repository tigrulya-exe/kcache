package ru.nsu.manasyan.kcache.config.stateholdermanager

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.core.state.holdermanager.RamStateHolderManager
import ru.nsu.manasyan.kcache.core.state.holdermanager.StateHolderManager
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Configuration rules for [StateHolder] beans
 */
@Configuration
@Import(
    value = [
        RedisStateHolderManagerConfiguration::class,
        HazelcastStateHolderManagerConfiguration::class
    ]
)
class StateHolderConfiguration {
    private val logger by LoggerProperty()

    /**
     * Creates [RamStateHolderManager] bean if
     * there was no [RamStateHolderManager] in context or kcache.state-holder
     * property's value in properties file is [KCacheProperties.StateHolder.RAM]
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "ram",
        matchIfMissing = true
    )
    fun ramStateHolderManager(): StateHolderManager {
        logger.debug("Building RamStateHolder")
        return RamStateHolderManager()
    }
}