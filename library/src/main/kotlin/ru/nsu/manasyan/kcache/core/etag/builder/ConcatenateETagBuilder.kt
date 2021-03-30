package ru.nsu.manasyan.kcache.core.etag.builder

import ru.nsu.manasyan.kcache.core.state.holdermanager.StateHolderManager
import ru.nsu.manasyan.kcache.properties.KCacheProperties

/**
 * Simple implementation of ETag builder, which concatenates tables' states.
 */
class ConcatenateETagBuilder(
    override val stateHolderManager: StateHolderManager,
    private val properties: KCacheProperties
) : ETagBuilder {
    companion object {
        const val DEFAULT_SEPARATOR = ":"
    }

    override fun buildETag(tableIds: List<String>, key: String): String {
        return tableIds.joinToString(separator = DEFAULT_SEPARATOR) {
            stateHolderManager.getOrCreateStateHolder(it)
                .mergeState(key, properties.defaultState)
        }
    }
}
