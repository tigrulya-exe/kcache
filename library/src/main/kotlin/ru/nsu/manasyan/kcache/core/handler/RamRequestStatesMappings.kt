package ru.nsu.manasyan.kcache.core.handler

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Storage of HTTP-request handler methods' names and
 * request metadata mappings located in RAM
 */
class RamRequestStatesMappings : RequestStatesMappings {
    private val statesMapping: ConcurrentMap<String, List<String>> = ConcurrentHashMap()

    override fun getRequestStates(requestName: String): List<String>? {
        return statesMapping[requestName]
    }

    override fun setRequestStates(requestName: String, states: List<String>) {
        statesMapping[requestName] = states
    }

    override fun getAllStates(): Map<String, List<String>> {
        return HashMap(statesMapping)
    }
}