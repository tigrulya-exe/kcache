package com.example.app.controller

import com.example.app.data.TestUser
import com.example.app.service.TestUserServiceKt
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.nsu.manasyan.kcache.core.annotations.KCacheable
import ru.nsu.manasyan.kcache.core.annotations.KCacheableJpa

@RequestMapping("/test")
@RestController
class TestController(private val service: TestUserServiceKt) {
//    @KCacheable(tables = ["users"])
    @KCacheableJpa(entities = [TestController::class])
    @GetMapping("/users")
    fun getUsers(
        @RequestHeader(name = HttpHeaders.IF_NONE_MATCH, required = false)
        ifNoneMatch: String?
    ): ResponseEntity<*> {
        return ResponseEntity.ok().body(
            service.getUsers()
        )
    }

    @GetMapping("/evict")
    fun evictUsersCache() {
        service.evictUsersCache()
    }

    @PostMapping("/users")
    fun addUser(@RequestBody user: TestUser) {
        service.addUser(user)
    }
}