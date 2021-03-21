package ru.nsu.manasyan.kcache.core.handler

import ru.nsu.manasyan.kcache.core.resultbuilder.ResponseEntityResultBuilderFactory
import ru.nsu.manasyan.kcache.core.resultbuilder.ResultBuilderFactory
import kotlin.reflect.KClass

class RequestHandlerMetadata(
    val tableStates: List<String> = mutableListOf(),

    val resultBuilderFactory: KClass<out ResultBuilderFactory> =
        ResponseEntityResultBuilderFactory::class,
)