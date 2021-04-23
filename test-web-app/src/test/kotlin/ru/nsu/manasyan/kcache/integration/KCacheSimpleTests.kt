package ru.nsu.manasyan.kcache.integration

import com.example.app.TestApplication
import com.example.app.data.TestUser
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(
    classes = [TestApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class KCacheSimpleTests(
    @Autowired restTemplate: TestRestTemplate
) {
    private val api = Api(restTemplate)

    companion object {
        const val DEFAULT_USER_ID = "TEST_ID"

        const val DEFAULT_USER_NAME = "TEST_NAME"

        const val DEFAULT_USER_AGE = 18
    }

    @Test
    fun `request contains etag`() {
        api.getUsers()
            .also { it.statusCode shouldBe HttpStatus.OK }
            .headers
            .eTag
            .also { it shouldNotBe null }
    }

    @Test
    fun `same etag value for response, depending on unchanged table`() {
        val eTag = api.getUsers()
            .also { it.statusCode shouldBe HttpStatus.OK }
            .headers
            .eTag
            .also { it shouldNotBe null }

        api.getUsers(eTag)
            .also {
                it.statusCode shouldBe HttpStatus.NOT_MODIFIED
                it.headers.eTag shouldBe eTag
            }
    }

    @Test
    fun `different etag value for response, depending on updated table`() {
        val eTag = api.getUsers()
            .also { it.statusCode shouldBe HttpStatus.OK }
            .headers
            .eTag
            .also { it shouldNotBe null }

        api.updateUser(getTestUser())
            .also { it.statusCode shouldBe HttpStatus.OK }

        val updatedETag = api.getUsers(eTag)
            .also { it.statusCode shouldBe HttpStatus.OK }
            .headers
            .eTag
            .also { it shouldNotBe eTag }

        api.getUsers(updatedETag)
            .also {
                it.statusCode shouldBe HttpStatus.NOT_MODIFIED
                it.headers.eTag shouldBe updatedETag
            }
    }

    private fun getTestUser() = TestUser(
        id = DEFAULT_USER_ID,
        name = DEFAULT_USER_NAME,
        age = DEFAULT_USER_AGE
    )
}