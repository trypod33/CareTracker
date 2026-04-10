package com.caretracker.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/** Room convenience class: load a Doctor and all Persons they are linked to. */
data class DoctorWithPersons(
    @Embedded val doctor: Doctor,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PersonDoctorCrossRef::class,
            parentColumn = "doctorId",
            entityColumn = "personId"
        )
    )
    val persons: List<Person>
)
