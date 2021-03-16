package ru.nsu.manasyan.kcache.defaults

import com.hazelcast.core.HazelcastInstance
import com.hazelcast.replicatedmap.ReplicatedMap
import ru.nsu.manasyan.kcache.core.StateHolder
import ru.nsu.manasyan.kcache.properties.HazelcastProperties

class HazelcastStateHolder(
    client: HazelcastInstance,
    properties: HazelcastProperties
) : StateHolder {
    private val states: ReplicatedMap<String, String> = client.getReplicatedMap(properties.mapName!!)

    override fun getState(tableId: String): String? {
        return states[tableId]
    }

    override fun setState(tableId: String, state: String) {
        states[tableId] = state
    }

    override fun removeState(tableId: String): Boolean {
        return states.remove(tableId) != null
    }

    override fun clear() {
        states.clear()
    }
}