package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val role: String,  // "doctor","nurse_practitioner","therapist","pharmacist","specialist","other"
    val specialty: String = "",
    val phone: String = "",
    val fax: String = "",
    val email: String = "",
    val address: String = "",
    val officeHours: String = "",
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
