package ru.nsu.manasyan.kcache.configs

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.manasyan.kcache.core.ETagBuilder
import ru.nsu.manasyan.kcache.core.StateHolder
import ru.nsu.manasyan.kcache.aspect.KCacheableAspect
import ru.nsu.manasyan.kcache.aspect.UpdateStateAspect
import ru.nsu.manasyan.kcache.defaults.ConcatenateETagBuilder
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
     * Создает аспект [KCacheableAspect] в случае,
     * если в контексте находятся экземпляры [StateHolder] и [ETagBuilder]
     */
    @Bean
    @ConditionalOnBean(value = [StateHolder::class, ETagBuilder::class])
    fun kCacheAspect(
        eTagBuilder: ETagBuilder
    ): KCacheableAspect {
        logger.debug("Building KCacheAspect")
        return KCacheableAspect(eTagBuilder)
    }

    /**
     * Создает аспект [UpdateStateAspect] в случае,
     * если в контексте находятся экземпляры [StateHolder] и [ETagBuilder]
     */
    @Bean
    @ConditionalOnBean(value = [StateHolder::class, ETagBuilder::class])
    fun updateStateAspect(
        stateHolder: StateHolder
    ): UpdateStateAspect {
        logger.debug("Building UpdateStateAspect")
        return UpdateStateAspect(stateHolder)
    }

}