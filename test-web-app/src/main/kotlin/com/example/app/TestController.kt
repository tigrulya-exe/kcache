package com.example.app

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import ru.nsu.manasyan.kcache.core.KCacheable

@RestController("test/")
class TestController (private val service: TestService) {
    @KCacheable(tables = ["users"])
    @GetMapping("Get")
    fun getUsers(@RequestHeader(name = HttpHeaders.IF_NONE_MATCH) ifNoneMatch: String): ResponseEntity<*> {
        return ResponseEntity.ok().body(service.getUsers())
    }
}

@Service
class TestService {
    fun getUsers(): List<TestUser> {
        return listOf(
                TestUser("ONE", 1),
                TestUser("TWO", 2),
                TestUser("THREE", 3),
                TestUser("FOUR", 4),
        )
    }
}

data class TestUser(val name: String, val age: Int)