package ru.nsu.manasyan.kcache.core.etag.builder

import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.properties.KCacheProperties

/**
 * Simple implementation of ETag builder, which concatenates tables' states.
 */
class ConcatenateETagBuilder(
    override val stateHolder: StateHolder,
    private val properties: KCacheProperties
) : ETagBuilder {
    companion object {
        const val DEFAULT_SEPARATOR = ":"
    }

    override fun buildETag(tableIds: List<String>): String {
        return tableIds.joinToString(separator = DEFAULT_SEPARATOR) {
            // TODO: mb refactor
            stateHolder.mergeState(it, properties.defaultState)
        }
    }
}
