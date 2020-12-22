package ru.nsu.manasyan.kcache.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(KCacheProperties.propertiesPrefix)
class KCacheProperties (
    val stateHolder: StateHolder?
) {
    companion object {
        const val propertiesPrefix = "kcache"
    }

    enum class StateHolder {
        RAM,
        REDIS
    }
    // TODO: add class for redis config
}