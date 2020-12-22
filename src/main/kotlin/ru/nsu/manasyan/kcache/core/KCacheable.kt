package ru.nsu.manasyan.kcache.core

import kotlin.reflect.KClass

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