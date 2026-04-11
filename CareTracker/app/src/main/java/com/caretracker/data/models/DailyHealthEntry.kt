package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One comprehensive health entry per person per day.
 * All fields are nullable — the user only fills what is relevant.
 * date is stored as "yyyy-MM-dd" string for easy grouping/querying.
 */
@Entity(tableName = "daily_health_entries")
data class DailyHealthEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val personId: Long,
    val date: String,                    // "yyyy-MM-dd"

    // Body measurements
    val weightValue: Float? = null,
    val weightUnit: String? = "lbs",     // "lbs" or "kg"
    val heartRate: Int? = null,          // bpm

    // Vitals
    val bpSystolic: Int? = null,
    val bpDiastolic: Int? = null,
    val bloodSugar: Float? = null,       // mg/dL

    // Blood pressure reading time (optional, stored as epoch ms)
    val bpTimestamp: Long? = null,
    // Blood sugar reading time (optional)
    val bsTimestamp: Long? = null,

    // Sleep
    val sleepHours: Float? = null,
    val sleepQuality: Int? = null,       // 1-10

    // Wellness
    val mood: Int? = null,               // 1-10
    val energyLevel: Int? = null,        // 1-10

    // Activity & Nutrition
    val steps: Int? = null,
    val exerciseMinutes: Int? = null,
    val waterOz: Float? = null,
    val calories: Int? = null,

    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
