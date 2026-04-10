package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class Appointment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val title: String,
    val description: String = "",
    val dateTime: Long,
    val durationMinutes: Int = 60,
    val location: String = "",
    val contactId: Long = 0,       // linked doctor/NP/therapist
    val appointmentType: String = "general", // "doctor","therapy","lab","dental","other"
    val isRecurring: Boolean = false,
    val recurrenceRule: String = "", // "DAILY","WEEKLY","BIWEEKLY","MONTHLY"
    val recurrenceEnd: Long = 0,
    val reminderMinutes: Int = 60,
    val notes: String = "",
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
