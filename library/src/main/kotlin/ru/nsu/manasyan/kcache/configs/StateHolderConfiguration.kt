package ru.nsu.manasyan.kcache.configs

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ConfigurationCondition
import ru.nsu.manasyan.kcache.core.StateHolder
import ru.nsu.manasyan.kcache.defaults.RamStateHolder
import ru.nsu.manasyan.kcache.defaults.RedisStateHolder
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Configuration rules for [StateHolder] beans
 */
@Configuration
class StateHolderConfiguration {
    private val logger by LoggerProperty()

    /**
     * Creates [RamStateHolder] bean if
     * there was no [RamStateHolder] in context or kcache.state-holder
     * property's value in properties file is [KCacheProperties.StateHolder.RAM]
     */
    @Bean
    @ConditionalOnMissingBean
    @Conditional(RamStateHolderConditional::class)
    fun ramStateHolder(): StateHolder {
        logger.debug("Building RamStateHolder")
        return RamStateHolder()
    }

    // TODO: get address and other props from properties file
    @Bean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "redis"
    )
    fun redisClient(): RedissonClient {
        logger.debug("Building RedissonClient")
        val config = Config()
        config.useSingleServer().address = "redis://127.0.0.1:6379"
        return Redisson.create(config)
    }

    /**
     * Creates [RedisStateHolder] bean if kcache.state-holder
     * property's value in properties file is [KCacheProperties.StateHolder.REDIS]
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedissonClient::class)
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "redis"
    )
    fun redisStateHolder(redisClient: RedissonClient): StateHolder {
        logger.debug("Building RedisStateHolder")
        return RedisStateHolder(redisClient)
    }
}

/**
 * Conditions under which [RamStateHolder] bean is created
 */
class RamStateHolderConditional : AnyNestedCondition(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN) {

    /**
     * Condition by default, when value of kcache.state-holder property in properties file wasn't set
     */
    @ConditionalOnProperty(
        matchIfMissing = true,
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "never"
    )
    object DefaultStateHolderCondition {
    }

    /**
     * Condition, value of kcache.state-holder property in properties file was set as [KCacheProperties.StateHolder.RAM]
     */
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "ram"
    )
    object StateHolderFromPropertiesCondition {
    }
}