package ru.nsu.manasyan.kcache.core.resultbuilder.hit

fun interface KCacheHitResultBuilder<D> {
    fun build(currentETag: String): D?
}