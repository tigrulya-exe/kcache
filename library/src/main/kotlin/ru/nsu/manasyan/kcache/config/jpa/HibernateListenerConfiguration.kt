package ru.nsu.manasyan.kcache.config.jpa

import org.hibernate.event.service.spi.EventListenerRegistry
import org.hibernate.event.spi.EventType
import org.hibernate.internal.SessionFactoryImpl
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.core.annotations.KCacheableJpa
import ru.nsu.manasyan.kcache.core.jpa.KCacheableEntitiesListener
import ru.nsu.manasyan.kcache.core.state.holdermanager.StateHolderManager
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceUnit


@Configuration
@ConditionalOnProperty(
    prefix = KCacheProperties.propertiesPrefix,
    name = ["jpa.listener.enable"],
    havingValue = "true"
)
class HibernateListenerConfiguration(
    @PersistenceUnit
    private val managerFactory: EntityManagerFactory,
) {
    private companion object {
        private const val ALL_PACKAGES = ""
    }

    private val logger by LoggerProperty()

    @Bean
    fun kCacheableEntitiesHibernateListener(
        stateHolderManager: StateHolderManager,
        stateProvider: NewStateProvider,
    ): KCacheableEntitiesListener = KCacheableEntitiesListener(
        stateHolderManager,
        stateProvider,
        getKCacheableEntities()
    ).also {
        initListener(it)
        logger.debug("KCacheableEntitiesListener was initialized")
    }

    private fun getKCacheableEntities() = Reflections(ALL_PACKAGES, MethodAnnotationsScanner())
        .getMethodsAnnotatedWith(KCacheableJpa::class.java)
        .flatMap { method ->
            method.getAnnotation(KCacheableJpa::class.java)
                .entities
                .asList()
        }.toSet()

    private fun initListener(listener: KCacheableEntitiesListener) {
        managerFactory.unwrap(SessionFactoryImpl::class.java)
            .serviceRegistry
            .getService(EventListenerRegistry::class.java)
            .apply {
                getEventListenerGroup(EventType.POST_INSERT).appendListener(listener)
                getEventListenerGroup(EventType.POST_UPDATE).appendListener(listener)
                getEventListenerGroup(EventType.POST_DELETE).appendListener(listener)
            }
    }
}