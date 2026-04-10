package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val birthDate: String? = null,
    val relationship: String? = null,
    val role: String? = null, // e.g., "Loved One", "Caregiver"
    val color: String = "#38bdf8",
    val avatar: String = "👤",
    val notes: String? = null
)