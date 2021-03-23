package ru.nsu.manasyan.kcache.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.core.state.provider.TimestampNewStateProvider
import ru.nsu.manasyan.kcache.properties.KCacheProperties

/**
 * Configuration rules for [NewStateProvider] beans
 */
@Configuration
class StateProviderConfiguration {
    @Bean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-provider"],
        havingValue = "timestamp",
        matchIfMissing = true
    )
    fun timestampNewStateProvider(): NewStateProvider {
        return TimestampNewStateProvider()
    }
}