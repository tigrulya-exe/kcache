package ru.nsu.manasyan.kcache.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import ru.nsu.manasyan.kcache.aspect.KCacheEvictAspect
import ru.nsu.manasyan.kcache.aspect.KCacheableAspect
import ru.nsu.manasyan.kcache.config.jpa.HibernateListenerConfiguration
import ru.nsu.manasyan.kcache.config.statestorage.StateHolderConfiguration
import ru.nsu.manasyan.kcache.core.etag.builder.ConcatenateETagBuilder
import ru.nsu.manasyan.kcache.core.etag.builder.ETagBuilder
import ru.nsu.manasyan.kcache.core.etag.extractor.ETagExtractor
import ru.nsu.manasyan.kcache.core.state.keyparser.KeyParser
import ru.nsu.manasyan.kcache.core.state.keyparser.SpelKeyParser
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
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
        StateHolderConfiguration::class,
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

    /**
     * Creates [KCacheableAspect] bean if
     * there are no [StateStorage] and [ETagBuilder] beans in context
     */
    @Bean
    fun kCacheAspect(
        eTagBuilder: ETagBuilder,
        extractor: ETagExtractor,
        keyParser: KeyParser
    ): KCacheableAspect {
        logger.debug("Building KCacheAspect")
        return KCacheableAspect(
            eTagBuilder,
            extractor,
            keyParser
        )
    }

    /**
     * Creates [KCacheEvictAspect] bean if
     * there are [StateStorage] and [ETagBuilder] beans in context
     */
    @Bean
    fun kCacheEvictAspect(
        stateStorageManager: StateStorageManager,
        newStateProvider: NewStateProvider,
        keyParser: KeyParser
    ): KCacheEvictAspect {
        logger.debug("Building KCacheEvictAspect")
        return KCacheEvictAspect(
            stateStorageManager,
            newStateProvider,
            keyParser
        )
    }

    @Bean
    fun spelExpressionParser(): ExpressionParser = SpelExpressionParser()
}