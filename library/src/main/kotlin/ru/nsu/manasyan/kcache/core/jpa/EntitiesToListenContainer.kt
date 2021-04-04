package ru.nsu.manasyan.kcache.core.jpa

import kotlin.reflect.KClass

class EntitiesToListenContainer(
    val entities: List<KClass<*>> = listOf()
)