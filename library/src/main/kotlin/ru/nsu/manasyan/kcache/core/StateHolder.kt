package ru.nsu.manasyan.kcache.core

/**
 * Entity, which contains information about current DB tables state
 */
interface StateHolder {
    /**
     * @return state of table with id == tableId or null if such not found
     */
    fun getState(tableId: String): String?

    /**
     * Sets a new state of table with id == tableId
     * @param state new state of table
     */
    fun setState(tableId: String, state: String)

    /**
     * Removes state of table with id == tableId
     * @return true if state was deleted, false otherwise
     */
    fun removeState(tableId: String): Boolean

    /**
     * Removes all tables' states
     */
    fun clear()
}