package ru.nsu.manasyan.kcache.core.state.storage

/**
 * Entity, which contains information about current DB tables state
 */
interface StateStorage {
    companion object {
        const val WHOLE_TABLE_KEY = ""
    }

    /**
     * @return state of table with id == tableId or null if such not found
     */
    fun getState(key: String): String?

    /**
     * Sets a new state of table with id == tableId
     * @param state new state of table
     */
    fun setState(key: String, state: String)

    /**
     * Removes state of table with id == tableId
     * @return true if state was deleted, false otherwise
     */
    fun removeState(key: String): Boolean

    /**
     * Removes all tables' states
     */
    fun clear()

    /**
     * If the specified key is not already associated with a value
     * or is associated with null, associates it with the given non-null value.
     * @return state of table with id == tableId
     */
    fun mergeState(key: String, default: String): String
}