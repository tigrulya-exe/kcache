package ru.nsu.manasyan.kcache.core

/**
 *
 * The entity responsible for constructing the ETag value
 */
interface ETagBuilder {
    val stateHolder: StateHolder

    /**
     * Function builds the ETag value based on the current state of DB tables
     * @param tableIds table ids, which state will be used during ETag construction
     */
    fun buildETag(tableIds: List<String>): String
}