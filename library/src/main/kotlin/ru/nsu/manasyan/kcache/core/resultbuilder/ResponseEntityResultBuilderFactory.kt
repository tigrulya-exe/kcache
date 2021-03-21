package ru.nsu.manasyan.kcache.core.resultbuilder

import ru.nsu.manasyan.kcache.core.resultbuilder.hit.ResponseEntityCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.ResponseEntityCacheMissResultBuilder

class ResponseEntityResultBuilderFactory : ResultBuilderFactory {
    private val onHitResultBuilder = ResponseEntityCacheHitResultBuilder()

    private val onMissResultBuilder = ResponseEntityCacheMissResultBuilder()

    override fun getOnHitResultBuilder() = onHitResultBuilder

    override fun getOnMissResultBuilder() = onMissResultBuilder

}