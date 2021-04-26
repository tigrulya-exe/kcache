package ru.nsu.manasyan.kcache.properties

import com.hazelcast.config.MulticastConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("${KCacheProperties.propertiesPrefix}.state-storage.hazelcast")
class HazelcastProperties(
    val host: String? = "localhost",
    val port: Int? = 5701,
    val discovery: HazelcastDiscoveryProperties = HazelcastDiscoveryProperties(),
)

class HazelcastDiscoveryProperties(
    val type: DiscoveryType? = DiscoveryType.MULTICAST,
    val multicast: MulticastDiscoveryConfig? = MulticastDiscoveryConfig(),
    val tcpIp: TcpIpDiscoveryConfig? = null
) {
    class TcpIpDiscoveryConfig(
        val members: Array<String>,
        val timeoutSeconds: Int = 5
    )

    class MulticastDiscoveryConfig(
        val group: String = MulticastConfig.DEFAULT_MULTICAST_GROUP,
        val port: Int = MulticastConfig.DEFAULT_MULTICAST_PORT
    )

    enum class DiscoveryType {
        TCP_IP,
        MULTICAST,
        // todo: experimental
        AUTO
    }
}
