package ru.nsu.manasyan.kcache.core.state.holdermanager

import com.hazelcast.core.HazelcastInstance
import ru.nsu.manasyan.kcache.core.state.holder.HazelcastStateHolder

class HazelcastStateHolderManager(
    private val client: HazelcastInstance
) : AbstractStateHolderManager() {
    override fun createStateHolder(stateHolderName: String) = HazelcastStateHolder(client, stateHolderName)
}