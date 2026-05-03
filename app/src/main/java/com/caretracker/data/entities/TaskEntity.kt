package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val description: String? = null,
    val priority: String = "medium",
    val status: String = "todo",
    val category: String = "personal",
    val dueDate: String? = null,
    val dueTime: String? = null,
    val estimatedMinutes: Int? = null,
    val tags: String? = null,
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)
