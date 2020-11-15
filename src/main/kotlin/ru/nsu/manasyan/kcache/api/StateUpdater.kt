package ru.nsu.manasyan.kcache.api

/**
 * Сущность, отвечающая за обновление текущего состоянии таблиц БД
 */
interface StateUpdater {
    /**
     * TODO: еще надо подумать
     * Мы должны заинджектить текущее состояние бд во всех апдейтеров
     */
    val stateHolder: StateHolder
}