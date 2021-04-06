package ru.nsu.manasyan.kcache.core.annotations

import ru.nsu.manasyan.kcache.core.resultbuilder.KCacheResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.ResponseEntityKCacheResultBuilder
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
annotation class KCacheableJpa(
    val entities: Array<KClass<*>>,

    val resultBuilder: KClass<out KCacheResultBuilder<out Any>> =
        ResponseEntityKCacheResultBuilder::class
)
