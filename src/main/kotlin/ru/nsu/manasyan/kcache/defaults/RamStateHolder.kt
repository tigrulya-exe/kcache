package ru.nsu.manasyan.kcache.defaults

import ru.nsu.manasyan.kcache.core.StateHolder
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Хранилище состояний таблиц БД в оперативной памяти
 */
class RamStateHolder : StateHolder {
    /**
     * Сущность, в которой хранятся состояния таблиц БД.
     * В качесте ключа используется id таблицы, в качестве значения - хэш состояния таблицы на данный момент
     */
    private val states: ConcurrentMap<String, String> = ConcurrentHashMap()

    override fun getState(tableId: String) = states[tableId]

    override fun setState(tableId: String, state: String) {
        states[tableId] = state
    }

    override fun removeState(tableId: String)= states.remove(tableId) != null

    override fun clear() = states.clear()
}
