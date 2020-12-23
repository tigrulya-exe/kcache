package ru.nsu.manasyan.kcache

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.nsu.manasyan.kcache.testapp.TestController

@SpringBootTest
class KCacheApplicationTests(
		@Autowired private val controller: TestController
) {

	@Test
	fun contextLoads() {
		println(controller.getUsers(""))
	}

}
