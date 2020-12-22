package ru.nsu.manasyan.kcache.configs

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ConfigurationCondition
import ru.nsu.manasyan.kcache.core.StateHolder
import ru.nsu.manasyan.kcache.properties.KCacheProperties
import ru.nsu.manasyan.kcache.defaults.RamStateHolder
import ru.nsu.manasyan.kcache.defaults.RedisStateHolder
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Правила создания бинов для [StateHolder]
 */
@Configuration
class StateHolderConfiguration {
    private val logger by LoggerProperty()

    /**
     * Создание бина RamStateHolder.
     * Эта функция вызовется, если пользователь не зарегистрировал в контексте
     * свою имплементацию интерфейса StateHolder или задал значение kcache.state-holder
     * в applications.yaml/properties, отличное от [KCacheProperties.StateHolder.RAM]
     */
    @Bean
    @ConditionalOnMissingBean
    @Conditional(RamStateHolderConditional::class)
    fun ramStateHolder(): StateHolder {
        logger.debug("Building RamStateHolder")
        return RamStateHolder()
    }

    /**
     * Создание бина RamStateHolder.
     * Эта функция вызовется, если пользователь не зарегистрировал в контексте
     * свою имплементацию интерфейса StateHolder или задал значение kcache.state-holder
     * в applications.yaml/properties, отличное от [KCacheProperties.StateHolder.RAM]
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "redis"
    )
    fun redisStateHolder(): StateHolder {
        logger.debug("Building RedisStateHolder")
        return RedisStateHolder()
    }
}

/**
 * Условия, при которых создается бин [RamStateHolder]
 */
class RamStateHolderConditional : AnyNestedCondition(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN) {

    /**
     * Случай по умолчанию, когда не задано значение kcache.state-holder в applications.yaml/properties
     */
    @ConditionalOnProperty(
        matchIfMissing = true,
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "never"
    )
    object DefaultStateHolderCondition {
    }

    /**
     * Случай, когда kcache.state-holder в applications.yaml/properties установлен как ram
     */
    @ConditionalOnProperty(
        prefix = KCacheProperties.propertiesPrefix,
        name = ["state-holder"],
        havingValue = "ram"
    )
    object StateHolderFromPropertiesCondition {
    }
}