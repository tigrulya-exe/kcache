package ru.nsu.manasyan.kcache.core.state.updater

import ru.nsu.manasyan.kcache.core.state.storage.StateStorage

/**
 * Entity which updates current states of DB tables
 */
interface StateUpdater {
    val stateStorage: StateStorage

    fun updateState(tableId: String, state: String) {
        stateStorage.setState(tableId, state)
    }
}