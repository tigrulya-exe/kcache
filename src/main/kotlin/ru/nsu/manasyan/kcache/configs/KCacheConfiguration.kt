package ru.nsu.manasyan.kcache.configs

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.api.ETagBuilder
import ru.nsu.manasyan.kcache.api.StateHolder
import ru.nsu.manasyan.kcache.aspect.KCacheAspect
import ru.nsu.manasyan.kcache.core.ConcatenateETagBuilder
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Класс-конфигуратор для стартера
 */
@Configuration
@EnableConfigurationProperties(KCacheProperties::class)
class KCacheAutoConfiguration {
    private val logger by LoggerProperty()

    /**
     * Создает дефолтный [ETagBuilder] в случае,
     * если в контексте находится экземпляр [StateHolder]
     * и отсутсвуют другие построители ETag (экземпляры [ETagBuilder])
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(value = [StateHolder::class])
    fun eTagBuilder(stateHolder: StateHolder): ETagBuilder {
        logger.debug("Building ConcatenateETagBuilder")
        return ConcatenateETagBuilder(stateHolder)
    }

    /**
     * Создает аспект [KCacheAspect] в случае,
     * если в контексте находятся экземпляры [StateHolder] и [ETagBuilder]
     */
    @Bean
    @ConditionalOnBean(value = [StateHolder::class, ETagBuilder::class])
    fun kCacheAspect(
        eTagBuilder: ETagBuilder
    ): KCacheAspect {
        logger.debug("Building KCacheAspect")
        return KCacheAspect(eTagBuilder)
    }
}