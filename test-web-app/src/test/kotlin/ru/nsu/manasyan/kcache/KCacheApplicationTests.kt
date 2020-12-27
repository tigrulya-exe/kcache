package ru.nsu.manasyan.kcache

import com.example.app.TestApplication
import com.example.app.data.TestUser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import java.net.URI
import java.util.*

@SpringBootTest(
    classes = [TestApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class KCacheSimpleApplicationTests(
    @Autowired private val restTemplate: TestRestTemplate
) {
    companion object {
        const val GET_USERS_PATH = "/test/users"

        const val ADD_USERS_PATH = "/test/users"
    }

    @Test
    fun `request contains etag`() {
        val usersResponse = getTestUsers()
        Assertions.assertEquals(HttpStatus.OK, usersResponse.statusCode)
        Assertions.assertNotNull(usersResponse.headers.eTag)
    }

    @Test
    fun `same etag value for response, depending on unchanged table`() {
        val firstResponse = getTestUsers()
        Assertions.assertEquals(HttpStatus.OK, firstResponse.statusCode)

        val eTag = firstResponse.headers.eTag
        Assertions.assertNotNull(eTag)

        val secondResponse = getTestUsers(eTag)
        Assertions.assertEquals(HttpStatus.NOT_MODIFIED, secondResponse.statusCode)
        Assertions.assertEquals(eTag, secondResponse.headers.eTag)
    }

    @Test
    fun `different etag value for response, depending on updated table`() {
        var getUsersResponse = getTestUsers()
        Assertions.assertEquals(HttpStatus.OK, getUsersResponse.statusCode)

        val eTag = getUsersResponse.headers.eTag
        Assertions.assertNotNull(eTag)

        val updateUserResponse = restTemplate.exchange<Void>(
            RequestEntity.post(URI.create(ADD_USERS_PATH))
                .body(getTestUser())
        )
        Assertions.assertEquals(HttpStatus.OK, updateUserResponse.statusCode)

        getUsersResponse = getTestUsers(eTag)
        Assertions.assertEquals(HttpStatus.OK, getUsersResponse.statusCode)

        val updatedETag = getUsersResponse.headers.eTag
        Assertions.assertNotEquals(eTag, updatedETag)

        getUsersResponse = getTestUsers(updatedETag)
        Assertions.assertEquals(HttpStatus.NOT_MODIFIED, getUsersResponse.statusCode)
        Assertions.assertEquals(updatedETag, getUsersResponse.headers.eTag)
    }

    private fun getTestUsers(previousETag: String? = null): ResponseEntity<*> {
        return restTemplate.exchange<ArrayList<TestUser>>(
            RequestEntity.get(
                URI.create(GET_USERS_PATH)
            ).apply {
                previousETag?.let {
                    this.header(HttpHeaders.IF_NONE_MATCH, previousETag)
                }
            }.build()
        )
    }

    private fun getTestUser() = TestUser(
        id = null,
        name = "TEST_NAME",
        age = 18
    )
}