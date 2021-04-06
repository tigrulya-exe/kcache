package ru.nsu.manasyan.kcache.core.state.storage

// TODO: add docs
interface StateStorageManager {
    fun getStateHolder(stateHolderName: String): StateStorage?

    fun getOrCreateStateHolder(stateHolderName: String): StateStorage
}