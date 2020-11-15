package ru.nsu.manasyan.kcache.api

/**
 * Сущность, в которой хранится вся информация о текущем состоянии таблиц БД
 */
interface StateHolder {
    /**
     * Получение состояние таблицы
     * @param tableId id таблицы
     */
    fun getState(tableId: String)

    /**
     * Установка нового состояния таблицы
     * @param tableId id таблицы
     * @param state новое состояние таблицы
     */
    fun setState(tableId: String, state: String)

    /**
     * Удаление состояние таблицы по id
     * @param tableId id таблицы
     */
    fun removeState(tableId: String)

    /**
     * Удаление всех состояний таблиц
     */
    fun clear()
}