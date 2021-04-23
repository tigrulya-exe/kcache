package ru.nsu.manasyan.kcache.core.etag.extractor

import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RequestHeader
import ru.nsu.manasyan.kcache.util.LoggerProperty
import java.lang.reflect.Method

class RequestHeaderETagExtractor : ETagExtractor {
    private val logger by LoggerProperty()

    /**
     * Function extracts If-None-Match HTTP-header's value from method's arguments.
     * i.e. argument, which was tagged with [RequestHeader] annotation with the name field value
     * equal to [HttpHeaders.IF_NONE_MATCH].
     * @return value of IF_NONE_MATCH argument, null if such not found.
     */
    override fun extract(method: Method, methodArgs: Array<Any>): String? {
        for ((currentParameter, parameterAnnotations) in method.parameterAnnotations.withIndex()) {
            for (parameterAnnotation in parameterAnnotations) {
                if (parameterAnnotation is RequestHeader
                    && parameterAnnotation.name == HttpHeaders.IF_NONE_MATCH
                ) {
                    return (methodArgs[currentParameter] as String?)
                        ?.removeQuotes()
                        ?.ifBlank {
                            logger.debug("Blank If-None-Match header value was found in method ${method.name}")
                            return null
                        }
                }
            }
        }
        return null
    }
}