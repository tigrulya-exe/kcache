package ru.nsu.manasyan.kcache.core.etag.extractor

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import java.lang.reflect.Method

class ResponseEntityIfNoneMatchHeaderExtractor : IfNoneMatchHeaderExtractor {

    /**
     * Function extracts If-None-Match HTTP-header's value from method's arguments.
     * i.e. from argument, which was instance of [ResponseEntity] annotation
     * with the [HttpHeaders.IF_NONE_MATCH] header.
     * @return value of IF_NONE_MATCH header, null if such not found.
     */
    override fun extract(method: Method, methodArgs: Array<Any>): String? {
        return methodArgs.filterIsInstance<ResponseEntity<*>>()
            .lastOrNull()
            ?.headers
            ?.ifNoneMatch
            ?.lastOrNull()
    }
}