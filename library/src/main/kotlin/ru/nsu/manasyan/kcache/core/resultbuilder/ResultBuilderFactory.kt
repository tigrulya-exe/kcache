package ru.nsu.manasyan.kcache.core.resultbuilder

import ru.nsu.manasyan.kcache.core.resultbuilder.hit.KCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.KCacheMissResultBuilder

interface ResultBuilderFactory {
    fun getOnHitResultBuilder(): KCacheHitResultBuilder<*>
    fun getOnMissResultBuilder(): KCacheMissResultBuilder<*>
}