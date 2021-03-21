package com.example.app.service

import com.example.app.data.TestUser
import com.example.app.repository.TestUserRepository
import org.springframework.stereotype.Service
import ru.nsu.manasyan.kcache.core.annotations.KCacheEvict

@Service
class TestUserServiceKt(
    private val repository: TestUserRepository
) {
    fun getUsers(): List<TestUser> {
        return repository.getUsers()
    }

    @KCacheEvict(tables = ["users"])
    fun addUser(user: TestUser) {
        repository.addUser(user)
    }

    @KCacheEvict(tables = ["users"])
    fun evictUsersCache() {}
}