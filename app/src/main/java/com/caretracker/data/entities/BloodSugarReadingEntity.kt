package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "blood_sugar_readings",
    foreignKeys = [ForeignKey(
        entity = HealthEntryEntity::class,
        parentColumns = ["id"],
        childColumns = ["entryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("entryId")]
)
data class BloodSugarReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val entryId: Long = 0L,
    val value: Float,
    val readingTime: String = "",
    val label: String = ""
)
