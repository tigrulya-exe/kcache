package ru.nsu.manasyan.kcache.core.resultbuilder

interface KCacheResultBuilder<R> {
    fun onCacheMiss(result: Any?, currentETag: String): R?

    fun onCacheHit(currentETag: String): R?
}