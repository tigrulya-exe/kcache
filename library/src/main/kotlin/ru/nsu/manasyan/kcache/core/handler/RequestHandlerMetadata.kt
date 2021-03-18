package ru.nsu.manasyan.kcache.core.handler

import ru.nsu.manasyan.kcache.core.resultbuilder.hit.KCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.hit.ResponseEntityCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.KCacheMissResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.ResponseEntityCacheMissResultBuilder
import kotlin.reflect.KClass

class RequestHandlerMetadata(
    val tableStates: List<String> = mutableListOf(),

    val onCacheHitResultBuilder: KClass<out KCacheHitResultBuilder<*>> =
        ResponseEntityCacheHitResultBuilder::class,

    val onCacheMissResultBuilder: KClass<out KCacheMissResultBuilder<*>> =
        ResponseEntityCacheMissResultBuilder::class
)