package ru.nsu.manasyan.kcache.config.state.storage

import com.hazelcast.config.Config
import com.hazelcast.config.JoinConfig
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.core.state.storage.StateStorageManager
import ru.nsu.manasyan.kcache.core.state.storage.hazelcast.HazelcastStateStorage
import ru.nsu.manasyan.kcache.core.state.storage.hazelcast.HazelcastStateStorageManager
import ru.nsu.manasyan.kcache.properties.HazelcastDiscoveryProperties
import ru.nsu.manasyan.kcache.properties.HazelcastDiscoveryProperties.DiscoveryType.MULTICAST
import ru.nsu.manasyan.kcache.properties.HazelcastDiscoveryProperties.DiscoveryType.TCP_IP
import ru.nsu.manasyan.kcache.properties.HazelcastProperties
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Configuration rules for [HazelcastStateStorage] beans
 */
@Configuration
@ConditionalOnProperty(
    prefix = KCacheProperties.propertiesPrefix,
    name = ["state-storage.name"],
    havingValue = "hazelcast"
)
@EnableConfigurationProperties(HazelcastProperties::class)
class HazelcastStateStorageManagerConfiguration {

    private val logger by LoggerProperty()

    /**
     * Creates [HazelcastStateStorageManager] bean if kcache.state-holder
     * property's value in properties file is [KCacheProperties.StateHolder.HAZELCAST]
     */
    @Bean
    fun hazelcastStateHolderManager(
        hazelcastClient: HazelcastInstance,
        properties: HazelcastProperties
    ): StateStorageManager {
        logger.debug("Building HazelcastStateHolder")
        return HazelcastStateStorageManager(hazelcastClient)
    }

    @Bean
    fun hazelcastClient(
        properties: HazelcastProperties
    ): HazelcastInstance {
        logger.debug("Building HazelcastInstance")
        val config = Config()
        when (properties.discovery.type) {
            MULTICAST -> configureMulticast(
                config.networkConfig.join,
                properties.discovery
            )
            TCP_IP -> configureTcpIp(
                config.networkConfig.join,
                properties.discovery
            )
            else -> TODO("AUTO DISCOVERY IS COMING SOON")
        }

        config.apply {
            networkConfig.port = properties.port!!
            networkConfig.publicAddress = properties.host
        }

        return Hazelcast.newHazelcastInstance(config)
    }

    private fun configureTcpIp(joinConfig: JoinConfig, discovery: HazelcastDiscoveryProperties) {
        discovery.tcpIp?.let {
            joinConfig.tcpIpConfig.apply {
                isEnabled = true
                members = it.members.toList()
                connectionTimeoutSeconds = it.timeoutSeconds
            }
            joinConfig.multicastConfig.isEnabled = false
        }
    }

    private fun configureMulticast(joinConfig: JoinConfig, discovery: HazelcastDiscoveryProperties) {
        discovery.multicast?.let {
            joinConfig.multicastConfig.apply {
                isEnabled = true
                multicastGroup = it.group
                multicastPort = it.port
            }
        }
    }

}