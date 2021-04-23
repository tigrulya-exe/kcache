package ru.nsu.manasyan.kcache.core.state.storage

abstract class AbstractStateStorageManager : StateStorageManager {
    private val stateHolders: MutableMap<String, StateStorage> = mutableMapOf()

    override fun getStateStorage(stateHolderName: String): StateStorage? =
        stateHolders[stateHolderName]

    override fun getOrCreateStateStorage(stateHolderName: String): StateStorage =
        getStateStorage(stateHolderName) ?: createStateHolder(stateHolderName).also {
            stateHolders[stateHolderName] = it
        }

    protected abstract fun createStateHolder(stateHolderName: String): StateStorage
}