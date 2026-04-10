package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habit_logs")
data class HabitLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val personId: Long,
    val logDate: String = "",   // "yyyy-MM-dd"
    val value: Int = 1,
    val notes: String = "",
    val loggedAt: Long = System.currentTimeMillis()
)
