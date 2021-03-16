package ru.nsu.manasyan.kcache.core.state.updater

import ru.nsu.manasyan.kcache.core.state.holder.StateHolder

/**
 * Entity which updates current states of DB tables
 */
interface StateUpdater {
    val stateHolder: StateHolder

    fun updateState(tableId: String, state: String) {
        stateHolder.setState(tableId, state)
    }
}