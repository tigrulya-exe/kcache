package ru.nsu.manasyan.kcache.core

/**
 * Сущность, отвечающая за обновление текущего состоянии таблиц БД
 */
interface StateUpdater {
    /**
     * TODO: еще надо подумать
     * Мы должны заинджектить текущее состояние бд во всех апдейтеров
     */
    val stateHolder: StateHolder

    /**
     * TODO: еще надо подумать, мб незачем это в метод выносить
     * Обновление состояния таблцы БД
     */
    fun updateState(tableId: String, state: String) {
        stateHolder.setState(tableId, state)
    }
}