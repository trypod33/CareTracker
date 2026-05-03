package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val email: String,
    val passwordHash: String,
    val displayName: String? = null,
    val avatarColor: String = "#4f9cf9",
    val role: String = "user",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
