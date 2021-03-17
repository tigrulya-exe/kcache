package ru.nsu.manasyan.kcache.core.resultbuilder.miss

fun interface KCacheMissResultBuilder<S, D> {
    fun build(functionResult: S?, currentETag: String): D?
}