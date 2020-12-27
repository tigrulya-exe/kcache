package com.example.app.controller

import com.example.app.data.TestUser
import com.example.app.service.TestUserService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.nsu.manasyan.kcache.core.KCacheable

@RequestMapping("/test")
@RestController
class TestController(private val service: TestUserService) {
    @KCacheable(tables = ["users"])
    @GetMapping("/users")
    fun getUsers(
        @RequestHeader(name = HttpHeaders.IF_NONE_MATCH, required = false)
        ifNoneMatch: String?
    ): ResponseEntity<*> {
        return ResponseEntity.ok().body(
            service.getUsers()
        )
    }

    @PostMapping("/users")
    fun addUser(@RequestBody user: TestUser) {
        service.addUser(user)
    }
}

