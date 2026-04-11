package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Stores individual timestamped vital readings.
 * Unlike DailyHealthEntry (one row per day), this table allows
 * multiple readings per day per metric — e.g. fasting BS, post-meal BS,
 * morning BP, evening BP, etc.
 *
 * readingType values: "BLOOD_PRESSURE", "BLOOD_SUGAR", "HEART_RATE", "WEIGHT", "OXYGEN_SAT", "TEMPERATURE"
 */
@Entity(
    tableName = "vital_readings",
    foreignKeys = [ForeignKey(
        entity = Person::class,
        parentColumns = ["id"],
        childColumns = ["personId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("personId"), Index("readingType"), Index("readingDate")]
)
data class VitalReading(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val personId: Long,

    /** yyyy-MM-dd — the calendar date of the reading */
    val readingDate: String,

    /** Epoch ms — the exact moment of the reading; defaults to midnight of readingDate if not set */
    val readingTimestamp: Long,

    /** One of: BLOOD_PRESSURE, BLOOD_SUGAR, HEART_RATE, WEIGHT, OXYGEN_SAT, TEMPERATURE */
    val readingType: String,

    // Blood Pressure
    val bpSystolic: Int? = null,
    val bpDiastolic: Int? = null,
    val bpPosition: String? = null,   // "Sitting", "Standing", "Lying"

    // Blood Sugar
    val bloodSugar: Float? = null,    // mg/dL
    val bsContext: String? = null,    // "Fasting", "Before Meal", "After Meal", "Bedtime", "Random"

    // Heart Rate
    val heartRate: Int? = null,       // bpm
    val hrContext: String? = null,    // "Resting", "After Exercise"

    // Weight
    val weightValue: Float? = null,
    val weightUnit: String? = "lbs",  // "lbs" or "kg"

    // Oxygen Saturation
    val oxygenSat: Int? = null,       // % SpO2

    // Temperature
    val temperature: Float? = null,
    val tempUnit: String? = "F",      // "F" or "C"

    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
