package ru.nsu.manasyan.kcache.core.jpa

import org.hibernate.event.spi.*
import org.hibernate.persister.entity.EntityPersister
import ru.nsu.manasyan.kcache.core.state.storage.StateStorage
import ru.nsu.manasyan.kcache.core.state.storage.StateStorageManager
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.util.LoggerProperty
import kotlin.reflect.KClass

class KCacheableEntitiesListener(
    private val stateStorageManager: StateStorageManager,
    private val stateProvider: NewStateProvider,
    private val entities: Set<KClass<*>>
) : PostInsertEventListener,
    PostUpdateEventListener,
    PostDeleteEventListener {

    private val logger by LoggerProperty()

    override fun requiresPostCommitHanding(persister: EntityPersister?): Boolean = false

    override fun onPostDelete(event: PostDeleteEvent?) {
        updateState(event?.entity)
    }

    override fun onPostUpdate(event: PostUpdateEvent?) {
        updateState(event?.entity)
    }

    override fun onPostInsert(event: PostInsertEvent?) {
        updateState(event?.entity)
    }

    private fun updateState(entity: Any?) = entity?.let {
        entity::class.let { clazz ->
            if (entities.contains(clazz)) {
                logger.debug("Updating $clazz")
                stateStorageManager
                    .getOrCreateStateStorage(clazz.qualifiedName!!)
                    .setState(
                        StateStorage.WHOLE_TABLE_KEY,
                        stateProvider.provide(clazz.qualifiedName!!)
                    )
            }
        }
    }

}