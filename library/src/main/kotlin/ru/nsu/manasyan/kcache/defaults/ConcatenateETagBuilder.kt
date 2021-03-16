package ru.nsu.manasyan.kcache.defaults

import ru.nsu.manasyan.kcache.core.ETagBuilder
import ru.nsu.manasyan.kcache.core.StateHolder
import ru.nsu.manasyan.kcache.exceptions.UnknownTableIdException

/**
 * Simple implementation of ETag builder, which concatenates tables' states.
 */
class ConcatenateETagBuilder(override val stateHolder: StateHolder) : ETagBuilder {
    companion object {
        // TODO: get value from application.yaml
        const val DEFAULT_SEPARATOR = ":"
    }

    override fun buildETag(tableIds: List<String>): String {
        return tableIds.joinToString(separator = DEFAULT_SEPARATOR) {
            stateHolder.getState(it) ?: throw UnknownTableIdException("Wrong table id: $it")
        }
    }
}
