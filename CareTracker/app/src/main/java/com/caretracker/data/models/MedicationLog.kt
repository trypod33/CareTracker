package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_logs")
data class MedicationLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val personId: Long,
    val takenAt: Long = System.currentTimeMillis(),
    val scheduledTime: String = "",
    val wasTaken: Boolean = true,
    val notes: String = ""
)
