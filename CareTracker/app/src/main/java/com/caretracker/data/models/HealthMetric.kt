package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_metrics")
data class HealthMetric(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String, // e.g., "Blood Sugar", "Mood", "Sleep"
    val unit: String?, // e.g., "mg/dL", "hours", "level"
    val type: String, // e.g., "numeric", "text", "selection"
    val defaultValue: String?, // e.g., "70-120", "Good", "8"
    val isDefault: Boolean = false, // True if this metric is pre-defined by the app
    val isEnabled: Boolean = true // Whether this metric is generally active for tracking
)