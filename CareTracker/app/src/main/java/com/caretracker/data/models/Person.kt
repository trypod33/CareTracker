package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "people")
data class Person(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val birthDate: String? = null,
    val relationship: String? = null,
    // "Self" | "Caregiver" | "Loved One"
    val role: String? = null,
    val color: String = "#38bdf8",
    val avatar: String = "\ud83d\udc64",
    val notes: String? = null,
    // true = this is the currently active profile
    val isActiveProfile: Boolean = false
)
