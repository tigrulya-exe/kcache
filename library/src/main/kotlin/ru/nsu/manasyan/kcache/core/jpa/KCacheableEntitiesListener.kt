package ru.nsu.manasyan.kcache.core.jpa

import org.hibernate.event.spi.*
import org.hibernate.persister.entity.EntityPersister
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.core.state.holdermanager.StateHolderManager
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.util.LoggerProperty
import kotlin.reflect.KClass

class KCacheableEntitiesListener(
    private val stateHolderManager: StateHolderManager,
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
                stateHolderManager
                    .getOrCreateStateHolder(clazz.qualifiedName!!)
                    .setState(
                        StateHolder.WHOLE_TABLE_KEY,
                        stateProvider.provide(clazz.qualifiedName!!)
                    )
            }
        }
    }

}