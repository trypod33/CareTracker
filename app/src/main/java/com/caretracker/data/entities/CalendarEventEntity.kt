package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calendar_events")
data class CalendarEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val title: String,
    val description: String? = null,
    val category: String = "personal",
    val color: String = "#4f9cf9",
    val startDatetime: Long,
    val endDatetime: Long? = null,
    val allDay: Boolean = false,
    val location: String? = null,
    val reminderMinutes: Int = 30,
    val recurrence: String = "none",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
