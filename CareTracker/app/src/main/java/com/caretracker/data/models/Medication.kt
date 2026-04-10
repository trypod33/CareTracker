package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val name: String,
    val dosage: String,
    val unit: String = "mg",
    val frequency: String,       // "daily", "twice_daily", "every_x_hours", "as_needed"
    val timesOfDay: List<String> = emptyList(), // List of times, e.g., ["08:00", "20:00"]
    val instructions: String = "",
    val prescribingDoctorId: Long = 0,
    val pillsRemaining: Int = 0,
    val pillsPerDose: Int = 1,
    val refillReminderAt: Int = 7, // remind when X pills remaining
    val startDate: String = "",
    val endDate: String = "",
    val isActive: Boolean = true,
    val color: String = "#4F98A3",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
