package ru.nsu.manasyan.kcache.core

/**
 * Entity which updates current states of DB tables
 */
interface StateUpdater {
    // TODO: We should inject current DB state into all StateUpdaters
    val stateHolder: StateHolder


    // TODO: mb remove
    fun updateState(tableId: String, state: String) {
        stateHolder.setState(tableId, state)
    }
}