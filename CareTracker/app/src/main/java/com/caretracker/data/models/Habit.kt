package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val name: String,
    val description: String = "",
    val frequency: String = "daily", // "daily","weekly","weekdays","weekends"
    val targetValue: Int = 1,
    val unit: String = "times",
    val color: String = "#6DAA45",
    val iconName: String = "star",
    val reminderTime: String = "",   // "HH:mm" or ""
    val isActive: Boolean = true,
    val startDate: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
