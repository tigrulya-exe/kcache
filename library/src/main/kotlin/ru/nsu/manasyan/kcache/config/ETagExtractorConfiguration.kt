package ru.nsu.manasyan.kcache.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.core.etag.extractor.IfNoneMatchHeaderExtractorComposite
import ru.nsu.manasyan.kcache.core.etag.extractor.RequestHeaderIfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.core.etag.extractor.ResponseEntityIfNoneMatchHeaderExtractor
import ru.nsu.manasyan.kcache.util.LoggerProperty

//TODO: add configs depending on application.yaml
/**
 * Configuration rules for [IfNoneMatchHeaderExtractor] beans
 */
@Configuration
class ETagExtractorConfiguration {
    private val logger by LoggerProperty()

    @Bean
    fun requestHeaderIfNoneMatchHeaderExtractor(): IfNoneMatchHeaderExtractor {
        return RequestHeaderIfNoneMatchHeaderExtractor()
    }

    @Bean
    fun responseEntityIfNoneMatchHeaderExtractor(): IfNoneMatchHeaderExtractor {
        return ResponseEntityIfNoneMatchHeaderExtractor()
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