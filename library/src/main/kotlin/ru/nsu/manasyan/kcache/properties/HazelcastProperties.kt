package ru.nsu.manasyan.kcache.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("${KCacheProperties.propertiesPrefix}.hazelcast")
class HazelcastProperties(
    val host: String? = "localhost",
    val port: Int? = 5701
)