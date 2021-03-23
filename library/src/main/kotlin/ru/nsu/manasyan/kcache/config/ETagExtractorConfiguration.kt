package ru.nsu.manasyan.kcache.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractorComposite
import ru.nsu.manasyan.kcache.core.etag.extractor.RequestEntityIfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.core.etag.extractor.RequestHeaderIfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.util.LoggerProperty

/**
 * Configuration rules for [IfNoneMatchHeaderExtractor] beans
 */
@Configuration
class ETagExtractorConfiguration {
    private val logger by LoggerProperty()

    @Bean
    @ConditionalOnProperty(
        value = ["kcache.header-extractor.spring-header.enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun requestHeaderIfNoneMatchHeaderExtractor(): IfNoneMatchHeaderExtractor {
        return RequestHeaderIfNoneMatchHeaderExtractor()
    }

    @Bean
    @ConditionalOnProperty(
        value = ["kcache.header-extractor.spring-request-entity.enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun responseEntityIfNoneMatchHeaderExtractor(): IfNoneMatchHeaderExtractor {
        return RequestEntityIfNoneMatchHeaderExtractor()
    }

    @Bean
    @Primary
    fun ifNoneMatchHeaderExtractorComposite(
        extractors: List<IfNoneMatchHeaderExtractor>
    ): IfNoneMatchHeaderExtractor {
        logger.debug("Applied IfNoneMatchHeaderExtractors: $extractors")
        return IfNoneMatchHeaderExtractorComposite(extractors)
    }

}