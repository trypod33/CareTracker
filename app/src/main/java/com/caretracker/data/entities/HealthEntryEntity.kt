package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_entries")
data class HealthEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val entryDate: String,

    val weight: Double? = null,
    val weightUnit: String? = "lbs",

    val heartRate: Int? = null,
    val bloodPressureSystolic: Int? = null,
    val bloodPressureDiastolic: Int? = null,
    val bloodSugar: Float? = null,

    val sleepHours: Float? = null,
    val sleepQuality: Int? = null,

    val mood: Int? = null,
    val moodScore: Int? = null,
    val energy: Int? = null,

    val steps: Int? = null,
    val exerciseMinutes: Int? = null,
    val waterOz: Float? = null,
    val calories: Int? = null,

    val notes: String? = null
)
