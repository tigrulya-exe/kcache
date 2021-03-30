package ru.nsu.manasyan.kcache.core.state.holder

import com.hazelcast.core.EntryEvent
import com.hazelcast.core.EntryListener
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.MapEvent
import com.hazelcast.replicatedmap.ReplicatedMap
import ru.nsu.manasyan.kcache.util.LoggerProperty

class HazelcastStateHolder(
    client: HazelcastInstance,
    stateHolderName: String
) : MapStateHolder(client.getReplicatedMap(stateHolderName)) {
    private val logger by LoggerProperty()

    init {
        if (logger.isDebugEnabled) {
            (states as ReplicatedMap<String, String>)
                .addEntryListener(ReplicatedMapEntryListener())
        }
    }

    override fun setAll(state: String) {
        // changing the values without a Map.put() is not reflected on the other members
        states.forEach { (key, _) ->
            states[key] = state
        }
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