package ru.nsu.manasyan.kcache.configs

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import ru.nsu.manasyan.kcache.properties.KCacheProperties

/**
 * Класс-конфигуратор для стартера
 */
@Configuration
// TODO мб лучше в спринг факторис прописать несколько конфигов, чем тут все импортить
@Import(StateHolderConfiguration::class)
@EnableConfigurationProperties(KCacheProperties::class)
class KCacheConfiguration