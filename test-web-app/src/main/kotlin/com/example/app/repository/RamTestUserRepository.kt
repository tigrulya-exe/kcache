package com.example.app.repository

import com.example.app.data.TestUser
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class RamTestUserRepository : TestUserRepository {
    // TODO: make concurrent
    private val users = mutableListOf(
        TestUser("ONE", "Peter", 21),
        TestUser("TWO", "Jane", 25),
        TestUser("THREE", "Jack", 63),
        TestUser("FOUR", "Anna", 49),
    )

    override fun getUsers(): List<TestUser> {
        return users
    }

    override fun addUser(user: TestUser) {
        users.add(
            TestUser(
                UUID.randomUUID().toString(),
                user.name,
                user.age
            )
        )
    }
}