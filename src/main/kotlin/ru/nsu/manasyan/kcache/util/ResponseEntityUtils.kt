package ru.nsu.manasyan.kcache.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * Функция копирует текущий [ResponseEntity] и вставляет в него
 * заголовкок ETag со значением eTag
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
         * Функция возвращает экземпляр [ResponseEntity]
         * с выставленным статусом 304 и заголовком ETag со значением eTag
         */
        fun notModified(eTag: String): ResponseEntity<*> {
            return ResponseEntity
                .status(HttpStatus.NOT_MODIFIED)
                .eTag(eTag)
                .build<Any>()
        }
    }
}