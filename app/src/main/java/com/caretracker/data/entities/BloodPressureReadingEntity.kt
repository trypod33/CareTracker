package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "blood_pressure_readings",
    foreignKeys = [ForeignKey(
        entity = HealthEntryEntity::class,
        parentColumns = ["id"],
        childColumns = ["entryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("entryId")]
)
data class BloodPressureReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val entryId: Long = 0L,
    val systolic: Int,
    val diastolic: Int,
    val readingTime: String = "",
    val label: String = ""
)
