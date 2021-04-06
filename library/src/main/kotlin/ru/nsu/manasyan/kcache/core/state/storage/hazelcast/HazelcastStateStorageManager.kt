package ru.nsu.manasyan.kcache.core.state.storage.hazelcast

import com.hazelcast.core.HazelcastInstance
import ru.nsu.manasyan.kcache.core.state.storage.AbstractStateStorageManager

class HazelcastStateStorageManager(
    private val client: HazelcastInstance
) : AbstractStateStorageManager() {
    override fun createStateHolder(stateHolderName: String) = HazelcastStateStorage(client, stateHolderName)
}