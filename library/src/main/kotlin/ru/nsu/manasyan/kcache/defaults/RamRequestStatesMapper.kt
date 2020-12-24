package ru.nsu.manasyan.kcache.defaults

import ru.nsu.manasyan.kcache.core.RequestStatesMapper
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class RamRequestStatesMapper : RequestStatesMapper {
    private val statesMapping: ConcurrentMap<String, Array<String>> = ConcurrentHashMap()

    override fun getStates(requestName: String): Array<String>? {
        return statesMapping[requestName]
    }

    override fun setStates(requestName: String, states: Array<String>) {
        statesMapping[requestName] = states
    }
}