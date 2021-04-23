package ru.nsu.manasyan.kcache.core.state.storage

// TODO: add docs
interface StateStorageManager {
    fun getStateStorage(stateHolderName: String): StateStorage?

    fun getOrCreateStateStorage(stateHolderName: String): StateStorage
}