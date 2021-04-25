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
class KCacheSimpleTests(
    @Autowired restTemplate: TestRestTemplate
) {
    private val apiAssertions = ApiAssertions(
        Api(restTemplate)
    )

    @Test
    fun `request contains etag`() {
        apiAssertions.checkNewETagInjected {
            getUsers()
        }
    }

    @Test
    fun `same etag value for response, depending on unchanged table`() {
        val eTag = apiAssertions.checkNewETagInjected {
            getUsers()
        }

        apiAssertions.checkStateNotModified(eTag) {
            getUsers(eTag)
        }
    }

    @Test
    fun `different etag value for response, depending on updated table`() {
        val eTag = apiAssertions.checkNewETagInjected {
            getUsers()
        }

        apiAssertions.checkStateEvicted {
            evictUsers()
        }

        val updatedETag = apiAssertions.checkNewETagInjected(eTag) {
            getUsers(eTag)
        }

        apiAssertions.checkStateNotModified(updatedETag) {
            getUsers(updatedETag)
        }
    }
}