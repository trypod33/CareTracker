package com.caretracker.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/** Room convenience class: load a Person and all their linked Doctors in one query. */
data class PersonWithDoctors(
    @Embedded val person: Person,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = PersonDoctorCrossRef::class,
            parentColumn = "personId",
            entityColumn = "doctorId"
        )
    )
    val doctors: List<Doctor>
)
