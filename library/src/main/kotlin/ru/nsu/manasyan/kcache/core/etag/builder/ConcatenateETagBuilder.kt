package ru.nsu.manasyan.kcache.core.etag.builder

import ru.nsu.manasyan.kcache.core.state.storage.StateStorageManager
import ru.nsu.manasyan.kcache.properties.KCacheProperties

/**
 * Simple implementation of ETag builder, which concatenates tables' states.
 */
class ConcatenateETagBuilder(
    override val stateStorageManager: StateStorageManager,
    private val properties: KCacheProperties
) : ETagBuilder {
    companion object {
        const val DEFAULT_SEPARATOR = ":"
    }

    override fun buildETag(tableIds: List<String>, key: String): String {
        return tableIds.joinToString(separator = DEFAULT_SEPARATOR) {
            stateStorageManager.getOrCreateStateHolder(it)
                .mergeState(key, properties.defaultState)
        }
    }
}
