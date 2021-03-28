package ru.nsu.kcache

import ru.nsu.manasyan.kcache.core.resultbuilder.ResponseEntityResultBuilderFactory

class RequestHandlerMetadata(
    val tableStates: List<String> = mutableListOf(),

    val resultBuilderFactory: String = ResponseEntityResultBuilderFactory::class.qualifiedName!!,

    val key: String = ""
)