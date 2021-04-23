package ru.nsu.manasyan.kcache.integration

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

        const val UPDATE_USERS_PATH = "/test/users"

        const val UPDATE_USER_PATH = "/test/users/%s"
    }

    fun getUsers(previousETag: String? = null) = get<ArrayList<TestUser>>(
        GET_USERS_PATH,
        previousETag
    )

    fun getUserById(id: String, previousETag: String? = null) = get<TestUser>(
        GET_USER_BY_ID_PATH.format(id),
        previousETag
    )

    fun updateUser(updatedUser: TestUser) = post<Void, TestUser>(UPDATE_USERS_PATH, updatedUser)

    fun updateUserById(updatedUser: TestUser) = post<Void, TestUser>(UPDATE_USER_PATH, updatedUser)

    private inline fun <reified R : Any, B : Any> post(
        path: String,
        body: B
    ): ResponseEntity<R> {
        return restTemplate.exchange(
            RequestEntity.post(URI.create(path)).body(body)
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