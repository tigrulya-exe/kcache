package ru.nsu.manasyan.kcache.defaults

import ru.nsu.manasyan.kcache.core.RequestStatesMapper
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class RamRequestStatesMapper : RequestStatesMapper {
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