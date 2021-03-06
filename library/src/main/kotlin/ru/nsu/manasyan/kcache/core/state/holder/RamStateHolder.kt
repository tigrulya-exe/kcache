package ru.nsu.manasyan.kcache.core.state.holder

import ru.nsu.manasyan.kcache.util.LoggerProperty
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * DB tables' states storage in RAM. Only for single instance usage.
 */
class RamStateHolder : StateHolder {
    private val logger by LoggerProperty()

    /**
     * Key - table id, value - table's hash
     */
    private val states: ConcurrentMap<String, String> = ConcurrentHashMap()

    override fun getState(tableId: String) = states[tableId]

    override fun setState(tableId: String, state: String) {
        logger.debug("Update state of $tableId by $state")
        states[tableId] = state
    }

    override fun removeState(tableId: String) = states.remove(tableId) != null

    override fun clear() = states.clear()
}
