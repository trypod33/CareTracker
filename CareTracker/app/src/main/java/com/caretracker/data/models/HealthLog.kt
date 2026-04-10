package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_logs")
data class HealthLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val personId: Long,
    val metricId: Long, // Foreign key to HealthMetric
    val value: String, // The recorded value (e.g., "110", "Happy", "7.5")
    val notes: String?,
    val timestamp: Long = System.currentTimeMillis()
)