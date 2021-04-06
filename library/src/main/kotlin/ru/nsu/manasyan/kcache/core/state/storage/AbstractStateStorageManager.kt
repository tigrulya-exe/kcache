package ru.nsu.manasyan.kcache.core.state.storage

abstract class AbstractStateStorageManager : StateStorageManager {
    private val stateHolders: MutableMap<String, StateStorage> = mutableMapOf()

    override fun getStateHolder(stateHolderName: String): StateStorage? =
        stateHolders[stateHolderName]

    override fun getOrCreateStateHolder(stateHolderName: String): StateStorage =
        getStateHolder(stateHolderName) ?: createStateHolder(stateHolderName).also {
            stateHolders[stateHolderName] = it
        }

    protected abstract fun createStateHolder(stateHolderName: String): StateStorage
}