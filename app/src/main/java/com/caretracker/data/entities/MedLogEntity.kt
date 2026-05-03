package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "med_logs")
data class MedLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val takenAt: Long = System.currentTimeMillis(),
    val takenDate: String,
    val scheduledTime: String? = null,
    val status: String = "taken",
    val note: String? = null,
    val doseTaken: String? = null
)
