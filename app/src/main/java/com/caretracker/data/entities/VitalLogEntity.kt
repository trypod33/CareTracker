package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vital_logs")
data class VitalLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val entryDate: String,
    val recordedAt: Long = System.currentTimeMillis(),
    val type: String,
    val value: Float? = null,
    val value2: Float? = null,
    val unit: String = "mg/dL",
    val notes: String? = null
)
