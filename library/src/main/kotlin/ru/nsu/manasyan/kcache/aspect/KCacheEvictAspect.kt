package ru.nsu.manasyan.kcache.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import ru.nsu.manasyan.kcache.core.annotations.KCacheEvict
import ru.nsu.manasyan.kcache.core.state.keyparser.KeyParser
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.core.state.storage.StateStorageManager
import ru.nsu.manasyan.kcache.util.LoggerProperty

@Aspect
class KCacheEvictAspect(
    private val stateStorageManager: StateStorageManager,
    private val newStateProvider: NewStateProvider,
    private val keyParser: KeyParser
) {
    private val logger by LoggerProperty()

    /**
     * Processes section of code, which changes the state of the DB.
     * Updates state of each DB table, listed in the tables field of [KCacheEvict]
     */
    @After("@annotation(kCacheEvict)")
    fun wrapKCacheEvictMethod(
        joinPoint: JoinPoint,
        kCacheEvict: KCacheEvict
    ) {
        kCacheEvict.tables.forEach { tableName ->
            val newState = newStateProvider.provide(tableName)
            stateStorageManager
                .getOrCreateStateStorage(tableName)
                .setState(
                    keyParser.parse(kCacheEvict.key, joinPoint.args),
                    newState
                )
            logger.debug("Table $tableName was updated: $newState")
        }
    }
}