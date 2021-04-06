package com.example.app.service

import com.example.app.data.TestUser
import com.example.app.repository.TestUserRepository
import org.springframework.stereotype.Service
import ru.nsu.manasyan.kcache.core.annotations.KCacheEvict
import java.util.*

@Service
class TestUserServiceKt(
    private val repository: TestUserRepository
) {
    fun getUsers(): List<TestUser> {
        return repository.findAll()
    }

    @KCacheEvict(tables = ["users"])
    fun addUser(user: TestUser) {
        repository.save(user)
    }

    fun generateUser() {
        repository.save(
            TestUser(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                777
            )
        )
    }

    @KCacheEvict(tables = ["users"])
    fun evictUsersCache() {
    }
}