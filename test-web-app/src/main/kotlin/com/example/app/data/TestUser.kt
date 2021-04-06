package com.example.app.data

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Users")
data class TestUser(
    @Id
    val id: String? = null,
    val name: String,
    val age: Int
)