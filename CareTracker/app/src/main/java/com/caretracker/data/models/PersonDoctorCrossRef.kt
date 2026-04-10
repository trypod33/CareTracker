package com.caretracker.data.models

import androidx.room.Entity
import androidx.room.Index

/**
 * Join table: one Doctor can be linked to many Persons, and
 * one Person can have many Doctors.
 */
@Entity(
    tableName = "person_doctor_links",
    primaryKeys = ["personId", "doctorId"],
    indices = [Index("doctorId")]
)
data class PersonDoctorCrossRef(
    val personId: Long,
    val doctorId: Long
)
