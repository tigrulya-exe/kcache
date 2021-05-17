package ru.nsu.manasyan.kcache.config.state.provider

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.core.state.provider.MySqlChecksumNewStateProvider
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.core.state.provider.PostgresMd5NewStateProvider
import ru.nsu.manasyan.kcache.core.state.provider.TimestampNewStateProvider
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import javax.sql.DataSource

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

    @Bean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-provider"],
        havingValue = "postgres-md5"
    )
    @ConditionalOnBean(DataSource::class)
    fun postgresMd5NewStateProvider(dataSource: DataSource): NewStateProvider {
        return PostgresMd5NewStateProvider(dataSource)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-provider"],
        havingValue = "mysql-checksum"
    )
    @ConditionalOnBean(DataSource::class)
    fun mySqlChecksumNewStateProvider(dataSource: DataSource): NewStateProvider {
        return MySqlChecksumNewStateProvider(dataSource)
    }
}