package ru.nsu.manasyan.kcache.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.aspectj.EnableSpringConfigured
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import ru.nsu.manasyan.kcache.aspect.KCacheEvictAspect
import ru.nsu.manasyan.kcache.aspect.KCacheableAspect
import ru.nsu.manasyan.kcache.config.jpa.HibernateListenerConfiguration
import ru.nsu.manasyan.kcache.config.statestorage.StateStorageConfiguration
import ru.nsu.manasyan.kcache.core.etag.builder.ConcatenateETagBuilder
import ru.nsu.manasyan.kcache.core.etag.builder.ETagBuilder
import ru.nsu.manasyan.kcache.core.state.keyparser.KeyParser
import ru.nsu.manasyan.kcache.core.state.keyparser.SpelKeyParser
import ru.nsu.manasyan.kcache.core.state.storage.StateStorage
import ru.nsu.manasyan.kcache.core.state.storage.StateStorageManager
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Auto configuration class for SpringBoot starter
 */
@Configuration
@Import(
    value = [
        StateStorageConfiguration::class,
        ETagExtractorConfiguration::class,
        StateProviderConfiguration::class,
        HibernateListenerConfiguration::class
    ]
)
@EnableConfigurationProperties(KCacheProperties::class)
class KCacheAutoConfiguration {
    private val logger by LoggerProperty()

    /**
     * Creates default [ETagBuilder] bean if
     * there is [StateStorage] instance in context
     * and there are no another [ETagBuilder] beans
     */
    @Bean
    @ConditionalOnMissingBean
    fun eTagBuilder(
        stateStorageManager: StateStorageManager,
        properties: KCacheProperties
    ): ETagBuilder {
        logger.debug("Building ConcatenateETagBuilder")
        return ConcatenateETagBuilder(stateStorageManager, properties)
    }

    @Bean
    fun spelKeyParser(expressionParser: ExpressionParser): KeyParser = SpelKeyParser(expressionParser)


    @Bean
    fun spelExpressionParser(): ExpressionParser = SpelExpressionParser()

    @Configuration
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["aop.type"],
        havingValue = "spring-aop",
        matchIfMissing = true
    )
    class KCacheSpringAOPConfiguration {
        private val logger by LoggerProperty()

        /**
         * Creates [KCacheableAspect] bean if
         * there are no [StateStorage] and [ETagBuilder] beans in context
         */
        @Bean
        fun kCacheAspect(): KCacheableAspect {
            logger.debug("Building KCacheAspect")
            return KCacheableAspect()
        }

        /**
         * Creates [KCacheEvictAspect] bean if
         * there are [StateStorage] and [ETagBuilder] beans in context
         */
        @Bean
        fun kCacheEvictAspect(): KCacheEvictAspect {
            logger.debug("Building KCacheEvictAspect")
            return KCacheEvictAspect()
        }
    }

    @Configuration
    @EnableSpringConfigured
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["aop.type"],
        havingValue = "aspectj"
    )
    class KCacheAspectJConfiguration
}