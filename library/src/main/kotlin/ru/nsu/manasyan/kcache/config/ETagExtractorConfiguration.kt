package ru.nsu.manasyan.kcache.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import ru.nsu.manasyan.kcache.core.etag.extractor.*
import ru.nsu.manasyan.kcache.util.LoggerProperty
import javax.servlet.http.HttpServletRequest

/**
 * Configuration rules for [ETagExtractor] beans
 */
@Configuration
class ETagExtractorConfiguration {
    private val logger by LoggerProperty()

    @Bean
    @ConditionalOnProperty(
        value = ["kcache.header-extractor.spring-context-request.enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun contextRequestETagExtractor(request: HttpServletRequest): ETagExtractor {
        return SpringContextRequestETagExtractor(request)
    }

    @Bean
    @ConditionalOnProperty(
        value = ["kcache.header-extractor.spring-header.enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun requestHeaderETagExtractor(): ETagExtractor {
        return RequestHeaderETagExtractor()
    }

    @Bean
    @ConditionalOnProperty(
        value = ["kcache.header-extractor.spring-request-entity.enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun requestEntityETagExtractor(): ETagExtractor {
        return RequestEntityETagExtractor()
    }

    @Bean
    @Primary
    fun ifNoneMatchHeaderExtractorComposite(
        extractors: List<ETagExtractor>
    ): ETagExtractor {
        logger.debug("Applied IfNoneMatchHeaderExtractors: $extractors")
        return ETagExtractorComposite(extractors)
    }

}