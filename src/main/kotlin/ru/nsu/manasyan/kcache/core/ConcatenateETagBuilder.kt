package ru.nsu.manasyan.kcache.core

import ru.nsu.manasyan.kcache.api.ETagBuilder
import ru.nsu.manasyan.kcache.api.StateHolder
import ru.nsu.manasyan.kcache.exceptions.UnknownTableIdException

/**
 * Простая имплементация строилтеля ETag, которая просто конкатенирует стейты таблиц.
 * Используется по умолчанию.
 */
class ConcatenateETagBuilder(override val stateHolder: StateHolder) : ETagBuilder {
    companion object {
        const val DEFAULT_SEPARATOR = ":"
    }

    override fun buildETag(tableIds: Array<String>): String {
        return tableIds.joinToString(separator = DEFAULT_SEPARATOR) {
            stateHolder.getState(it) ?: throw UnknownTableIdException("Wrong table id: $it")
        }
    }
}
