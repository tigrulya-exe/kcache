package com.example.app.controller

import com.example.app.data.TestUser
import com.example.app.service.TestUserServiceKt
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.nsu.manasyan.kcache.core.annotations.KCacheable

@RequestMapping("/test/users")
@RestController
class TestController(private val service: TestUserServiceKt) {
    @KCacheable(tables = ["users"])
    @GetMapping
    fun getUsers(
        @RequestHeader(name = HttpHeaders.IF_NONE_MATCH, required = false)
        ifNoneMatch: String?
    ): ResponseEntity<*> {
        return ResponseEntity.ok().body(
            service.getUsers()
        )
    }

    @KCacheable(tables = ["users"])
    @GetMapping("/context-request")
    fun getUsers(): ResponseEntity<*> {
        return ResponseEntity.ok().body(
            service.getUsers()
        )
    }

    @GetMapping("/evict")
    fun evictUsersCache() {
        service.evictUsersCache()
    }

    @PostMapping
    fun addUser(@RequestBody user: TestUser) {
        service.addUser(user)
    }
}