package ru.nsu.manasyan.kcache.core

/**
 * Сущность, отвечающая за построение значения ETag
 */
interface ETagBuilder {
    val stateHolder: StateHolder

    /**
     * Построение значение ETag по текущему состоянию таблиц
     * @param tableIds id таблиц, состояние которых необходимо использовать для построения ETag
     */
    fun buildETag(tableIds: List<String>): String
}