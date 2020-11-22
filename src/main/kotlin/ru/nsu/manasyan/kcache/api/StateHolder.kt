package ru.nsu.manasyan.kcache.api

/**
 * Сущность, в которой хранится вся информация о текущем состоянии таблиц БД
 */
interface StateHolder {
    /**
     * Получение состояние таблицы
     * @param tableId id таблицы
     * @return Состояние таблицы или null если состояния нет
     */
    fun getState(tableId: String): String?

    /**
     * Установка нового состояния таблицы
     * @param tableId id таблицы
     * @param state новое состояние таблицы
     */
    fun setState(tableId: String, state: String)

    /**
     * Удаление состояние таблицы по id
     * @param tableId id таблицы
     * @return true в случае успешного удаления, false если таблицы с таким id не существует
     */
    fun removeState(tableId: String): Boolean

    /**
     * Удаление всех состояний таблиц
     */
    fun clear()
}