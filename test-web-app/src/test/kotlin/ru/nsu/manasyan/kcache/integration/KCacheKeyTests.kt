package ru.nsu.manasyan.kcache.integration

import com.example.app.TestApplication
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(
    classes = [TestApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
class KCacheKeyTests(
    @Autowired restTemplate: TestRestTemplate
) {
    private val api = Api(restTemplate)

    @Test
    fun `request contains etag`() {

    }
}