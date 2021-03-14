package ru.nsu.manasyan.kcache.configs

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ConfigurationCondition
import ru.nsu.manasyan.kcache.core.StateHolder
import ru.nsu.manasyan.kcache.defaults.HazelcastStateHolder
import ru.nsu.manasyan.kcache.defaults.RamStateHolder
import ru.nsu.manasyan.kcache.defaults.RedisStateHolder
import ru.nsu.manasyan.kcache.properties.HazelcastProperties
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.properties.RedisProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

import com.hazelcast.config.Config as HazelcastConfig
import org.redisson.config.Config as RedissonConfig

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

    @Bean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "redis"
    )
    fun redisClient(
        properties: RedisProperties
    ): RedissonClient {
        logger.debug("Building RedissonClient")
        val config = RedissonConfig()
        config.useSingleServer().address = "redis://${properties.host}:${properties.port}"
        return Redisson.create(config)
    }

    @Bean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "hazelcast"
    )
    fun hazelcastClient(
        properties: HazelcastProperties
    ): HazelcastInstance {
        logger.debug("Building HazelcastInstance")
        val config = HazelcastConfig()
        config.apply {
            networkConfig.port = properties.port!!
            networkConfig.publicAddress = properties.host
        }
        return Hazelcast.newHazelcastInstance()
    }

    /**
     * Creates [HazelcastStateHolder] bean if kcache.state-holder
     * property's value in properties file is [KCacheProperties.StateHolder.HAZELCAST]
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(HazelcastInstance::class)
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "hazelcast"
    )
    fun hazelcastStateHolder(
        hazelcastClient: HazelcastInstance,
        properties: HazelcastProperties
    ): StateHolder {
        logger.debug("Building HazelcastStateHolder")
        return HazelcastStateHolder(hazelcastClient, properties)
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
        // TODO: refactor
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