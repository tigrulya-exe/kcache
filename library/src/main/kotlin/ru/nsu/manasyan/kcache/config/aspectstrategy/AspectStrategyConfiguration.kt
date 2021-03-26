package ru.nsu.manasyan.kcache.config.aspectstrategy

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.annotation.RequestScope
import ru.nsu.manasyan.kcache.aspect.strategy.KCacheableAspectStrategy
import ru.nsu.manasyan.kcache.aspect.strategy.ReflectionMetadataStrategy
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

@Import(GeneratedMetadataStrategyConfiguration::class)
@Configuration
class AspectStrategyConfiguration {
    private val logger by LoggerProperty()

    @RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
    @Bean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["aspect.strategy"],
        havingValue = "reflection-metadata",
        matchIfMissing = true
    )
    fun kCacheAspectStrategy(): KCacheableAspectStrategy {
        logger.debug("Building ReflectionMetadataStrategy")
        return ReflectionMetadataStrategy()
    }
}