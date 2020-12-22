package ru.nsu.manasyan.kcache.defaults

import ru.nsu.manasyan.kcache.core.StateHolder
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * DB tables' states storage in RAM
 */
class RamStateHolder : StateHolder {
    /**
     * Key - table id, value - table's hash
     */
    private val states: ConcurrentMap<String, String> = ConcurrentHashMap()

    override fun getState(tableId: String) = states[tableId]

    override fun setState(tableId: String, state: String) {
        states[tableId] = state
    }

    override fun removeState(tableId: String) = states.remove(tableId) != null

    override fun clear() = states.clear()
}
