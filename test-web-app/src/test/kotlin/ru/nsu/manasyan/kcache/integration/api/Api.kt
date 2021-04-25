package ru.nsu.manasyan.kcache.integration.api

import com.example.app.data.TestUser
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpHeaders
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import java.net.URI

class Api(
    private val restTemplate: TestRestTemplate
) {
    companion object PATH {
        const val GET_USERS_PATH = "/test/users"

        const val GET_USER_BY_ID_PATH = "/test/users/%s"

        const val EVICT_USERS = "/test/users/evict"

        const val EVICT_USER_BY_ID = "/test/users/%s/evict"
    }

    fun getUsers(previousETag: String? = null) = get<ArrayList<TestUser>>(
        GET_USERS_PATH,
        previousETag
    )

    fun getUserById(id: String, previousETag: String? = null) = get<TestUser>(
        GET_USER_BY_ID_PATH.format(id),
        previousETag
    )

    fun evictUsers() = post<Void, Void>(EVICT_USERS)

    fun evictUserById(id: String) = post<Void, Void>(EVICT_USER_BY_ID.format(id))

    private inline fun <reified R : Any, B : Any> post(
        path: String,
        body: B? = null
    ): ResponseEntity<R> {
        return restTemplate.exchange(
            RequestEntity.post(URI.create(path)).let { builder ->
                body?.let { builder.body(body) } ?: builder.build()
            }
        )
    }

    private inline fun <reified R : Any> get(
        path: String,
        previousETag: String? = null
    ): ResponseEntity<R> {
        return restTemplate.exchange(
            RequestEntity.get(
                URI.create(path)
            ).apply {
                previousETag?.let {
                    this.header(HttpHeaders.IF_NONE_MATCH, previousETag)
                }
            }.build()
        )
    }
}