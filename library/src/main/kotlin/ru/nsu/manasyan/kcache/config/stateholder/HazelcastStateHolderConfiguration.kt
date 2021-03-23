package ru.nsu.manasyan.kcache.config.stateholder

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.core.state.holder.HazelcastStateHolder
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.properties.HazelcastProperties
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Configuration rules for [HazelcastStateHolder] beans
 */
@Configuration
@ConditionalOnProperty(
    prefix = KCacheProperties.propertiesPrefix,
    name = ["state-holder"],
    havingValue = "hazelcast"
)
@EnableConfigurationProperties(HazelcastProperties::class)
class HazelcastStateHolderConfiguration {

    private val logger by LoggerProperty()

    @Bean
    fun hazelcastClient(
        properties: HazelcastProperties
    ): HazelcastInstance {
        logger.debug("Building HazelcastInstance")
        val config = Config()
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
    fun hazelcastStateHolder(
        hazelcastClient: HazelcastInstance,
        properties: HazelcastProperties
    ): StateHolder {
        logger.debug("Building HazelcastStateHolder")
        return HazelcastStateHolder(hazelcastClient, properties)
    }

}