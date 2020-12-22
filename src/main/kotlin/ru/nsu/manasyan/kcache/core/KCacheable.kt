package ru.nsu.manasyan.kcache.core

import com.github.matfax.klassindex.IndexAnnotated
import kotlin.reflect.KClass

@IndexAnnotated
@Target(AnnotationTarget.FUNCTION)
annotation class KCacheable(
    /**
     * Таблицы, состояние которых должно измениться при вызове аннотированного участка кода
     */
    val tables: Array<String> = [],

    /**
     * TODO: временное решение
     * JPA сущности, состояние которых должно измениться при вызове аннотированного участка кода
     */
    val entities: Array<KClass<*>> = []
)

