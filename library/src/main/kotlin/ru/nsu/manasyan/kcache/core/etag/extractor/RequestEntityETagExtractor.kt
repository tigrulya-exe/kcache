package ru.nsu.manasyan.kcache.core.etag.extractor

import org.springframework.http.HttpHeaders
import org.springframework.http.RequestEntity
import java.lang.reflect.Method

class RequestEntityETagExtractor : ETagExtractor {

    /**
     * Function extracts If-None-Match HTTP-header's value from method's arguments.
     * i.e. from argument, which was instance of [RequestEntity] annotation
     * with the [HttpHeaders.IF_NONE_MATCH] header.
     * @return value of IF_NONE_MATCH header, null if such not found.
     */
    override fun extract(method: Method, methodArgs: Array<Any>): String? {
        return methodArgs.filterIsInstance<RequestEntity<*>>()
            .lastOrNull()
            ?.headers
            ?.ifNoneMatch
            ?.lastOrNull()
            ?.removeQuotes()
    }
}