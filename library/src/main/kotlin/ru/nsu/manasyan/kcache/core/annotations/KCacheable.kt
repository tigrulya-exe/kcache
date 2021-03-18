package ru.nsu.manasyan.kcache.core.annotations

import ru.nsu.manasyan.kcache.core.resultbuilder.hit.KCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.hit.ResponseEntityCacheHitResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.KCacheMissResultBuilder
import ru.nsu.manasyan.kcache.core.resultbuilder.miss.ResponseEntityCacheMissResultBuilder
import kotlin.reflect.KClass

// TODO: add description from aspect
/**
 * Enables HTTP-caching of request which was processing by current method
 */
@Target(AnnotationTarget.FUNCTION)
annotation class KCacheable(
    /**
     * Tables on which the return value of the HTTP-request handler method depends
     */
    val tables: Array<String> = [],

    val onCacheHitResultBuilder: KClass<out KCacheHitResultBuilder<*>> =
        ResponseEntityCacheHitResultBuilder::class,

    val onCacheMissResultBuilder: KClass<out KCacheMissResultBuilder<*>> =
        ResponseEntityCacheMissResultBuilder::class,

    /**
     * TODO: временное решение
     * TODO: сделать отдельную аннотацию , в которой принимать Entity вместо имен таблиц
     *  в качестве мета аннотации поставить над ней KCacheable, чтобы аспекты сработали
     *  обрабатывать данную аннотацию в процессоре аннотаций и складывать имена JPA-классов
     *  вместо имен таблиц в генерируемый файл
     * JPA сущности, состояние которых должно измениться при вызове аннотированного участка кода
     */
    val entities: Array<KClass<*>> = []
)