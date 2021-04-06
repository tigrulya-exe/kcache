package ru.nsu.manasyan.kcache.core.etag.builder

import ru.nsu.manasyan.kcache.core.state.storage.StateStorageManager

/**
 *
 * The entity responsible for constructing the ETag value
 */
interface ETagBuilder {
    val stateStorageManager: StateStorageManager

    // todo: fix docs
    /**
     * Function builds the ETag value based on the current state of DB tables
     * @param tableIds table ids, which state will be used during ETag construction
     */
    fun buildETag(tableIds: List<String>, key: String): String
}