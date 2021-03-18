package ru.nsu.manasyan.kcache.core.resultbuilder.miss

fun interface KCacheMissResultBuilder<D> {
    // TODO: try to parametrize functionResult class
    fun build(functionResult: Any?, currentETag: String): D?
}