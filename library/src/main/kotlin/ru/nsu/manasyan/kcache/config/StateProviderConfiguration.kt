package ru.nsu.manasyan.kcache.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.core.state.provider.TimestampNewStateProvider

/**
 * Configuration rules for [NewStateProvider] beans
 */
//TODO: add configs depending on application.yaml
@Configuration
class StateProviderConfiguration {
    @Bean
    fun timestampNewStateProvider(): NewStateProvider {
        return TimestampNewStateProvider()
    }
}