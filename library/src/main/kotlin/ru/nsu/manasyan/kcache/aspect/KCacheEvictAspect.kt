package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import ru.nsu.manasyan.kcache.core.annotations.KCacheEvict
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.util.LoggerProperty

@Aspect
class KCacheEvictAspect(
    private val stateHolder: StateHolder,
    private val newStateProvider: NewStateProvider
) {
    private val logger by LoggerProperty()

    /**
     * Processes section of code, which changes the state of the DB.
     * Updates state of each DB table, listed in the tables field of [KCacheEvict]
     */
    @After("@annotation(ru.nsu.manasyan.kcache.core.annotations.KCacheEvict)")
    fun wrapKCacheEvictMethod(joinPoint: JoinPoint) {
        val method = (joinPoint.signature as MethodSignature).method
        // we know, that method has KCacheEvict annotation
        val updatedTables = getAnnotationInstance<KCacheEvict>(method)!!.tables

        updatedTables.forEach { tableName ->
            val newState = newStateProvider.provide(tableName)
            stateHolder.setState(tableName, newState)
            logger.debug("Table $tableName was updated: $newState")
        }
    }
}