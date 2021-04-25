package com.example.app.repository

import com.example.app.data.TestUser
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@ConditionalOnProperty(
    name = ["repository.user.type"],
    havingValue = "ram"
)
class RamTestUserRepository : TestUserRepository {
    // TODO: make concurrent
    private val users = mutableMapOf(
        "one" to TestUser("ONE", "Peter", 21),
        "two" to TestUser("TWO", "Jane", 25),
        "three" to TestUser("THREE", "Jack", 63),
        "four" to TestUser("FOUR", "Anna", 49),
    )

    override fun findAll(): List<TestUser> {
        return users.values.toList()
    }

    override fun save(user: TestUser) {
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