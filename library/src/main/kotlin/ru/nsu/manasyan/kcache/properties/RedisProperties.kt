package ru.nsu.manasyan.kcache.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("${KCacheProperties.propertiesPrefix}.state-storage.redis")
class RedisProperties(
    val host: String?,
    val port: Int?
)