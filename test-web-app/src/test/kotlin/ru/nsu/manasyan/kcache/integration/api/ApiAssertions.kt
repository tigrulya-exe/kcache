package ru.nsu.manasyan.kcache.integration.api

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ApiAssertions(
    private val api: Api
) {
    fun <R> checkStateEvicted(action: Api.() -> ResponseEntity<R>) = api.action()
        .also { it.statusCode shouldBe HttpStatus.OK }

    fun <R> checkStateNotModified(expectedETag: String?, action: Api.() -> ResponseEntity<R>) = api.action()
        .also {
            it.statusCode shouldBe HttpStatus.NOT_MODIFIED
            it.headers.eTag shouldBe expectedETag
        }

    fun <R> checkNewETagInjected(previousETag: String? = null, action: Api.() -> ResponseEntity<R>) = api.action()
        .also { it.statusCode shouldBe HttpStatus.OK }
        .headers
        .eTag
        .also { it shouldNotBe previousETag }
}

