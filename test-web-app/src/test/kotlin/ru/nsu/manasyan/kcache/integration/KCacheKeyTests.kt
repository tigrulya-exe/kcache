package ru.nsu.manasyan.kcache.integration

import com.example.app.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import ru.nsu.manasyan.kcache.integration.api.Api
import ru.nsu.manasyan.kcache.integration.api.ApiAssertions

@SpringBootTest(
    classes = [TestApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class KCacheKeyTests(
    @Autowired restTemplate: TestRestTemplate
) {
    companion object {
        const val TEST_FIRST_USER_ID = "one"

        const val TEST_SECOND_USER_ID = "two"
    }

    private val apiAssertions = ApiAssertions(
        Api(restTemplate)
    )

    @Test
    fun `request contains etag`() {
        apiAssertions.checkNewETagInjected {
            getUserById(TEST_FIRST_USER_ID)
        }
    }

    @Test
    fun `same etag value for response, depending on unchanged part of table`() {
        val eTag = apiAssertions.checkNewETagInjected {
            getUserById(TEST_FIRST_USER_ID)
        }

        apiAssertions.checkStateNotModified(eTag) {
            getUserById(TEST_FIRST_USER_ID, eTag)
        }
    }

    @Test
    fun `different etag value for response, depending on updated part of table`() {
        val eTag = apiAssertions.checkNewETagInjected {
            getUserById(TEST_FIRST_USER_ID)
        }

        apiAssertions.checkStateEvicted {
            evictUserById(TEST_FIRST_USER_ID)
        }

        val updatedETag = apiAssertions.checkNewETagInjected {
            getUserById(TEST_FIRST_USER_ID, eTag)
        }

        apiAssertions.checkStateNotModified(updatedETag) {
            getUserById(TEST_FIRST_USER_ID, updatedETag)
        }
    }

    @Test
    fun `updating single user's state doesn't update other's state`() {
        var firstUserETag = apiAssertions.checkNewETagInjected {
            getUserById(TEST_FIRST_USER_ID)
        }

        val secondUserETag = apiAssertions.checkNewETagInjected {
            getUserById(TEST_SECOND_USER_ID)
        }

        apiAssertions.checkStateEvicted {
            evictUserById(TEST_FIRST_USER_ID)
        }

        apiAssertions.checkStateNotModified(secondUserETag) {
            getUserById(TEST_SECOND_USER_ID, secondUserETag)
        }

        firstUserETag = apiAssertions.checkNewETagInjected {
            getUserById(TEST_FIRST_USER_ID, firstUserETag)
        }

        apiAssertions.checkStateEvicted {
            evictUserById(TEST_SECOND_USER_ID)
        }

        apiAssertions.checkStateNotModified(firstUserETag) {
            getUserById(TEST_FIRST_USER_ID, firstUserETag)
        }
    }

    @Test
    fun `updating single user's state update all users' state`() {
        val firstUserETag = apiAssertions.checkNewETagInjected {
            getUserById(TEST_FIRST_USER_ID)
        }

        val allUsersETag = apiAssertions.checkNewETagInjected {
            getUsers()
        }

        apiAssertions.checkStateEvicted {
            evictUserById(TEST_FIRST_USER_ID)
        }

        apiAssertions.checkNewETagInjected(firstUserETag) {
            getUserById(TEST_FIRST_USER_ID, firstUserETag)
        }

        apiAssertions.checkNewETagInjected(allUsersETag) {
            getUsers(allUsersETag)
        }
    }

    @Test
    fun `updating all users' state update single user's state`() {
        val allUsersETag = apiAssertions.checkNewETagInjected {
            getUsers()
        }

        val firstUserETag = apiAssertions.checkNewETagInjected {
            getUserById(TEST_FIRST_USER_ID)
        }

        apiAssertions.checkStateEvicted {
            evictUsers()
        }

        apiAssertions.checkNewETagInjected(firstUserETag) {
            getUserById(TEST_FIRST_USER_ID, firstUserETag)
        }

        apiAssertions.checkNewETagInjected(allUsersETag) {
            getUsers(allUsersETag)
        }
    }
}