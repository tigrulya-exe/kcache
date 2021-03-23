package ru.nsu.manasyan.kcache.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.nsu.manasyan.kcache.aspect.KCacheEvictAspect
import ru.nsu.manasyan.kcache.aspect.KCacheableAspect
import ru.nsu.manasyan.kcache.aspect.strategy.KCacheableAspectStrategy
import ru.nsu.manasyan.kcache.config.aspectstrategy.AspectStrategyConfiguration
import ru.nsu.manasyan.kcache.config.stateholder.StateHolderConfiguration
import ru.nsu.manasyan.kcache.core.etag.builder.ConcatenateETagBuilder
import ru.nsu.manasyan.kcache.core.etag.builder.ETagBuilder
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.core.state.holder.StateHolder
import ru.nsu.manasyan.kcache.core.state.provider.NewStateProvider
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
        AspectStrategyConfiguration::class
    ]
)
@EnableConfigurationProperties(KCacheProperties::class)
class KCacheAutoConfiguration {
    private val logger by LoggerProperty()

    /**
     * Creates default [ETagBuilder] bean if
     * there is [StateHolder] instance in context
     * and there are no another [ETagBuilder] beans
     */
    @Bean
    @ConditionalOnMissingBean
    fun eTagBuilder(stateHolder: StateHolder): ETagBuilder {
        logger.debug("Building ConcatenateETagBuilder")
        return ConcatenateETagBuilder(stateHolder)
    }

    /**
     * Creates [KCacheableAspect] bean if
     * there are no [StateHolder] and [ETagBuilder] beans in context
     */
    @Bean
    fun kCacheAspect(
        eTagBuilder: ETagBuilder,
        extractor: IfNoneMatchHeaderExtractor,
        strategy: KCacheableAspectStrategy
    ): KCacheableAspect {
        logger.debug("Building KCacheAspect")
        return KCacheableAspect(
            eTagBuilder,
            extractor,
            strategy
        )
    }

    /**
     * Creates [KCacheEvictAspect] bean if
     * there are [StateHolder] and [ETagBuilder] beans in context
     */
    @Bean
    fun kCacheEvictAspect(
        stateHolder: StateHolder,
        newStateProvider: NewStateProvider
    ): KCacheEvictAspect {
        logger.debug("Building KCacheEvictAspect")
        return KCacheEvictAspect(stateHolder, newStateProvider)
    }

}