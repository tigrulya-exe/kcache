package ru.nsu.manasyan.kcache.config.stateholdermanager

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.core.state.holder.RedisStateHolder
import ru.nsu.manasyan.kcache.core.state.holdermanager.RedisStateHolderManager
import ru.nsu.manasyan.kcache.core.state.holdermanager.StateHolderManager
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.properties.RedisProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Configuration rules for [RedisStateHolder] beans
 */
@Configuration
@ConditionalOnProperty(
    prefix = KCacheProperties.propertiesPrefix,
    name = ["state-holder"],
    havingValue = "redis"
)
@EnableConfigurationProperties(RedisProperties::class)
class RedisStateHolderManagerConfiguration {

    private val logger by LoggerProperty()

    @Bean
    fun redisClient(
        properties: RedisProperties
    ): RedissonClient {
        logger.debug("Building RedissonClient")
        val config = Config()
        config.useSingleServer().address = "redis://${properties.host}:${properties.port}"
        return Redisson.create(config)
    }

    /**
     * Creates [RedisStateHolderManager] bean if kcache.state-holder
     * property's value in properties file is [KCacheProperties.StateHolder.REDIS]
     */
    @Bean
    fun redisStateHolder(redisClient: RedissonClient): StateHolderManager {
        logger.debug("Building RedisStateHolder")
        return RedisStateHolderManager(redisClient)
    }
}