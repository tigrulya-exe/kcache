package com.example.app.controller

import com.example.app.data.TestUser
import com.example.app.service.TestUserServiceKt
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.nsu.manasyan.kcache.core.annotations.KCacheableJpa

@RequestMapping("/test/users")
@RestController
class TestJpaController(private val service: TestUserServiceKt) {
    @KCacheableJpa(entities = [TestUser::class])
    @GetMapping("/jpa")
    fun getUsers(
        @RequestHeader(name = HttpHeaders.IF_NONE_MATCH, required = false)
        ifNoneMatch: String?
    ): ResponseEntity<*> {
        return ResponseEntity.ok().body(
            service.getUsers()
        )
    }

    @GetMapping("/generate")
    fun generateUser() {
        service.generateUser()
    }
}