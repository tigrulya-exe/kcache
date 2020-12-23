package ru.nsu.manasyan.kcache.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * @return new [ResponseEntity] instance with content of current [ResponseEntity]
 * and affixed ETag HTTP-header value
 */
fun ResponseEntity<*>.withEtag(eTag: String) : ResponseEntity<*> {
    return ResponseEntity
        .status(this.statusCode)
        .headers(this.headers)
        .eTag(eTag)
        .body(this.body)
}

class EtagResponseBuilder {
    companion object {
        /**
         * @return [ResponseEntity] instance with response code 304
         * and affixed ETag HTTP-header value
         */
        fun notModified(eTag: String): ResponseEntity<*> {
            return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .eTag(eTag)
                .build<Any>()
        }
    }
}