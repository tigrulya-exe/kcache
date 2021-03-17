package ru.nsu.manasyan.kcache.core.handler

import ru.nsu.manasyan.kcache.core.resultbuilder.hit.KCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.hit.ResponseEntityCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.KCacheMissResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.ResponseEntityCacheMissResultBuilder
import kotlin.reflect.KClass

class RequestHandlerMetadata {
    private val tableStates: List<String> = mutableListOf()

    private val onCacheHitResultBuilder: KClass<out KCacheHitResultBuilder<*>> =
        ResponseEntityCacheHitResultBuilder::class

    private val onCacheMissResultBuilder: KClass<out KCacheMissResultBuilder<*, *>> =
        ResponseEntityCacheMissResultBuilder::class
}