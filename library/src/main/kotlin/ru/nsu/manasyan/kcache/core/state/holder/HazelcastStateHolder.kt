package ru.nsu.manasyan.kcache.core.state.holder

import com.hazelcast.core.EntryEvent
import com.hazelcast.core.EntryListener
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.MapEvent
import com.hazelcast.replicatedmap.ReplicatedMap
import ru.nsu.manasyan.kcache.properties.HazelcastProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

class HazelcastStateHolder(
    client: HazelcastInstance,
    properties: HazelcastProperties
) : StateHolder {
    private val states: ReplicatedMap<String, String> = client.getReplicatedMap(properties.mapName!!)

    private val logger by LoggerProperty()

    init {
        if (logger.isDebugEnabled) {
            states.addEntryListener(ReplicatedMapEntryListener())
        }
    }

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

    override fun mergeState(tableId: String, default: String): String {
        return states.merge(tableId, default) { _, new -> new }!!
    }
}

class ReplicatedMapEntryListener : EntryListener<String, String> {
    private val logger by LoggerProperty()

    override fun entryAdded(event: EntryEvent<String, String>?) = entryEventHandler(event)

    override fun entryUpdated(event: EntryEvent<String, String>?) = entryEventHandler(event)

    override fun entryRemoved(event: EntryEvent<String, String>?) = entryEventHandler(event)

    override fun entryEvicted(event: EntryEvent<String, String>?) = entryEventHandler(event)

    override fun entryExpired(event: EntryEvent<String, String>?) = entryEventHandler(event)

    override fun mapCleared(event: MapEvent?) = mapEventHandlerEvicted(event)

    override fun mapEvicted(event: MapEvent?) = mapEventHandlerEvicted(event)

    private fun entryEventHandler(event: EntryEvent<String, String>?) {
        logger.debug("Map '${event?.name}' was ${event?.eventType}: ${event?.key}, ${event?.value}")
    }

    private fun mapEventHandlerEvicted(event: MapEvent?) {
        logger.debug("Map '${event?.name}' was ${event?.eventType} ")
    }
}