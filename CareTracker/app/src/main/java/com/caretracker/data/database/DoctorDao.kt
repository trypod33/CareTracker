package com.caretracker.data.database

import androidx.room.*
import com.caretracker.data.models.Doctor
import com.caretracker.data.models.DoctorWithPersons
import com.caretracker.data.models.PersonDoctorCrossRef
import com.caretracker.data.models.PersonWithDoctors
import kotlinx.coroutines.flow.Flow

@Dao
interface DoctorDao {

    // ── Doctor CRUD ──────────────────────────────────────────────────────────

    @Query("SELECT * FROM doctors ORDER BY name ASC")
    fun getAllDoctors(): Flow<List<Doctor>>

    @Query("SELECT * FROM doctors WHERE id = :id")
    suspend fun getDoctorById(id: Long): Doctor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDoctor(doctor: Doctor): Long

    @Update
    suspend fun updateDoctor(doctor: Doctor)

    @Delete
    suspend fun deleteDoctor(doctor: Doctor)

    // ── Cross-ref (link / unlink) ─────────────────────────────────────────────

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun linkDoctorToPerson(crossRef: PersonDoctorCrossRef)

    @Delete
    suspend fun unlinkDoctorFromPerson(crossRef: PersonDoctorCrossRef)

    @Query("DELETE FROM person_doctor_links WHERE personId = :personId AND doctorId = :doctorId")
    suspend fun unlinkByIds(personId: Long, doctorId: Long)

    // ── Filtered queries ──────────────────────────────────────────────────────

    /** All doctors linked to a specific person. */
    @Transaction
    @Query("SELECT * FROM people WHERE id = :personId")
    fun getDoctorsForPerson(personId: Long): Flow<PersonWithDoctors>

    /** All persons linked to a specific doctor. */
    @Transaction
    @Query("SELECT * FROM doctors WHERE id = :doctorId")
    fun getPersonsForDoctor(doctorId: Long): Flow<DoctorWithPersons>

    /**
     * IDs of doctors already linked to a person.
     * Used to pre-check boxes in AddEditDoctorActivity.
     */
    @Query("SELECT doctorId FROM person_doctor_links WHERE personId = :personId")
    suspend fun getLinkedDoctorIds(personId: Long): List<Long>

    /**
     * IDs of PERSONS already linked to a doctor.
     * Used in AddEditDoctorActivity to pre-check which members see this doctor.
     * (Bug fix: previously this was calling getLinkedDoctorIds with a doctorId,
     *  returning doctor IDs instead of person IDs.)
     */
    @Query("SELECT personId FROM person_doctor_links WHERE doctorId = :doctorId")
    suspend fun getLinkedPersonIds(doctorId: Long): List<Long>
}
