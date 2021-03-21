package com.example.app.repository

import com.example.app.data.TestUser

interface TestUserRepository {
    fun getUsers(): List<TestUser>

    fun addUser(user: TestUser)

    fun findUserById(id: String): TestUser?
}