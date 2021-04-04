package ru.nsu.manasyan.kcache.core.annotations

import ru.nsu.manasyan.kcache.core.resultbuilder.ResponseEntityResultBuilderFactory
import ru.nsu.manasyan.kcache.core.resultbuilder.ResultBuilderFactory
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class KCacheableJpa(
    val entities: Array<KClass<*>>,

    val resultBuilderFactory: KClass<out ResultBuilderFactory> =
        ResponseEntityResultBuilderFactory::class
)
