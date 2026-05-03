package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_logs")
data class HabitLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val loggedDate: String,
    val count: Int = 1,
    val note: String? = null,
    val loggedAt: Long = System.currentTimeMillis()
)
