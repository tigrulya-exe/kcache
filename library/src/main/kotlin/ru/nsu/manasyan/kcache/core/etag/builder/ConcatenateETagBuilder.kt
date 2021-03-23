package ru.nsu.manasyan.kcache.core.etag.builder

import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.exceptions.UnknownTableIdException

/**
 * Simple implementation of ETag builder, which concatenates tables' states.
 */
class ConcatenateETagBuilder(override val stateHolder: StateHolder) : ETagBuilder {
    companion object {
        const val DEFAULT_SEPARATOR = ":"
    }

    override fun buildETag(tableIds: List<String>): String {
        return tableIds.joinToString(separator = DEFAULT_SEPARATOR) {
            // TODO: use here mergeState
            stateHolder.getState(it) ?: throw UnknownTableIdException("Wrong table id: $it")
        }
    }
}
