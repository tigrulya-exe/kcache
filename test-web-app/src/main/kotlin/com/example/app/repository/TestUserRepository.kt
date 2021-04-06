package com.example.app.repository

import com.example.app.data.TestUser

interface TestUserRepository {
    fun findAll(): List<TestUser>

    fun save(user: TestUser)

    fun findUserById(id: String): TestUser?
}