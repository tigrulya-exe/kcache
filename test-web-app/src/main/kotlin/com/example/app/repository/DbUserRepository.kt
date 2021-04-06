package com.example.app.repository

import com.example.app.data.TestUser
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(
    name = ["repository.user.type"],
    havingValue = "jpa",
    matchIfMissing = true
)
interface DbUserRepository : TestUserRepository, JpaRepository<TestUser, Int>