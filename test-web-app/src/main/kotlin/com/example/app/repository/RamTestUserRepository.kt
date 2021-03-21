package com.example.app.repository

import com.example.app.data.TestUser
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RamTestUserRepository : TestUserRepository {
    // TODO: make concurrent
    private val users = mutableMapOf(
        "ONE" to TestUser("ONE", "Peter", 21),
        "TWO" to TestUser("TWO", "Jane", 25),
        "THREE" to TestUser("THREE", "Jack", 63),
        "FOUR" to TestUser("FOUR", "Anna", 49),
    )

    override fun getUsers(): List<TestUser> {
        return users.values.toList()
    }

    override fun addUser(user: TestUser) {
        UUID.randomUUID().toString().apply {
            users[this] = TestUser(
                this,
                user.name,
                user.age
            )
        }
    }

    override fun findUserById(id: String): TestUser? {
        return users[id]
    }
}