package ru.nsu.manasyan.kcache.core

import kotlin.reflect.KClass

/**
 * Indicates, that this section of code changes the state of the DB
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UpdateState(

    /**
     * Tables, whose state should change when annotated code section will be called
     */
    val tables: Array<String>,

    /**
     * TODO: временное решение
     * JPA сущности, состояние которых должно измениться при вызове аннотированного участка кода
     */
    val entities: Array<KClass<*>> = []

)