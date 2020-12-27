package com.example.app.service

import com.example.app.data.TestUser
import com.example.app.repository.TestUserRepository
import org.springframework.stereotype.Service
import ru.nsu.manasyan.kcache.core.UpdateState

@Service
class TestUserService(
    private val repository: TestUserRepository
) {
    fun getUsers(): List<TestUser> {
        return repository.getUsers()
    }

    @UpdateState(tables = ["users"])
    fun addUser(user: TestUser) {
        repository.addUser(user)
    }
}