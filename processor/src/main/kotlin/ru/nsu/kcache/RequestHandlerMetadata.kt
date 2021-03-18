package ru.nsu.kcache

import ru.nsu.manasyan.kcache.core.resultbuilder.hit.ResponseEntityCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.ResponseEntityCacheMissResultBuilder

class RequestHandlerMetadata(
    val tableStates: List<String> = mutableListOf(),

    val onCacheHitResultBuilder: String = ResponseEntityCacheHitResultBuilder::class.qualifiedName!!,

    val onCacheMissResultBuilder: String = ResponseEntityCacheMissResultBuilder::class.qualifiedName!!
)