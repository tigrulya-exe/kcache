package ru.nsu.manasyan.kcache.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(KCacheProperties.propertiesPrefix)
class KCacheProperties(
    val stateHolder: StateHolder?,
    val defaultState: String = "1970-01-01T00:00:00Z"
) {
    companion object {
        const val propertiesPrefix = "kcache"
    }

    enum class StateHolder {
        RAM,
        REDIS,
        HAZELCAST
    }
}