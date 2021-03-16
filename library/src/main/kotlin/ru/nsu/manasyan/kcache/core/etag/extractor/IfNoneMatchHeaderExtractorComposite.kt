package ru.nsu.manasyan.kcache.core.etag.extractor

import ru.nsu.manasyan.kcache.util.LoggerProperty
import java.lang.reflect.Method

class IfNoneMatchHeaderExtractorComposite(
    val extractors: MutableList<IfNoneMatchHeaderExtractor> = mutableListOf()
) : IfNoneMatchHeaderExtractor {
    private val logger by LoggerProperty()

    override fun extract(method: Method, methodArgs: Array<Any>): String? {
        extractors.forEach {
            it.extract(method, methodArgs)?.let { ifNoneMatchHeaderValue ->
                return ifNoneMatchHeaderValue
            }
        }

        logger.debug("If-None-Match header value wasn't found in method ${method.name}")
        return null
    }
}
