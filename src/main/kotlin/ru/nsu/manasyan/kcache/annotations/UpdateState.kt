package ru.nsu.manasyan.kcache.annotations

import kotlin.reflect.KClass

/**
 * Аннотация, указывающая, что данный участок кода меняет состояние БД
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UpdateState(

    /**
     * Таблицы, состояние которых должно измениться при вызове аннотированного участка кода
     */
    val tables: Array<String>,

    /**
     * TODO: временное решение
     * JPA сущности, состояние которых должно измениться при вызове аннотированного участка кода
     */
    val entities: KClass<*>

)