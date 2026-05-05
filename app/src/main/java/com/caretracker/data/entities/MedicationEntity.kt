package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val name: String,
    val genericName: String? = null,
    val dosage: String? = null,
    val dosageUnit: String = "mg",
    val form: String = "tablet",
    val frequency: String? = null,
    val timesPerDay: Int = 1,
    val scheduledTimes: String? = null,
    val withFood: Boolean = false,
    val instructions: String? = null,
    val prescriber: String? = null,
    val pharmacy: String? = null,
    val rxNumber: String? = null,
    val color: String = "#4f9cf9",
    val currentCount: Int = 0,
    val pillsPerRefill: Int? = null,
    val refillReminderAt: Int = 7,
    val lastRefillDate: String? = null,
    val nextRefillDate: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val sortOrder: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
