package ru.nsu.manasyan.kcache.config.stateholder

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.nsu.manasyan.kcache.core.state.holder.RamStateHolder
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Configuration rules for [StateHolder] beans
 */
@Configuration
@Import(
    value = [
        RedisStateHolderConfiguration::class,
        HazelcastStateHolderConfiguration::class
    ]
)
class StateHolderConfiguration {
    private val logger by LoggerProperty()

    /**
     * Creates [RamStateHolder] bean if
     * there was no [RamStateHolder] in context or kcache.state-holder
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
    fun ramStateHolder(): StateHolder {
        logger.debug("Building RamStateHolder")
        return RamStateHolder()
    }
}