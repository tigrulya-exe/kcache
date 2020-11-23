package ru.nsu.manasyan.kcache.api

/**
 * Сущность, отвечающая за построение значения ETag
 */
interface ETagBuilder {
    val stateHolder: StateHolder

    /**
     * Построение значение ETag по текущему состоянию таблиц
     * @param tableIds id таблиц, состояние которых необходимо использовать для построения ETag
     */
    fun buildETag(tableIds: Iterable<String>): String

    // TODO мб добавить
    //  fun buildETag(controllerMethodName: String)
}