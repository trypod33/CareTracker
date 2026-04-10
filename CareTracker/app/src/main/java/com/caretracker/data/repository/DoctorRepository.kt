package com.caretracker.data.repository

import com.caretracker.data.database.DoctorDao
import com.caretracker.data.models.Doctor
import com.caretracker.data.models.DoctorWithPersons
import com.caretracker.data.models.PersonDoctorCrossRef
import com.caretracker.data.models.PersonWithDoctors
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DoctorRepository @Inject constructor(private val doctorDao: DoctorDao) {

    fun getAllDoctors(): Flow<List<Doctor>> = doctorDao.getAllDoctors()

    suspend fun getDoctorById(id: Long): Doctor? = doctorDao.getDoctorById(id)

    suspend fun insertDoctor(doctor: Doctor): Long = doctorDao.insertDoctor(doctor)

    suspend fun updateDoctor(doctor: Doctor) = doctorDao.updateDoctor(doctor)

    suspend fun deleteDoctor(doctor: Doctor) = doctorDao.deleteDoctor(doctor)

    // ── Linking ───────────────────────────────────────────────────────────────

    suspend fun linkDoctorToPerson(personId: Long, doctorId: Long) =
        doctorDao.linkDoctorToPerson(PersonDoctorCrossRef(personId, doctorId))

    suspend fun unlinkDoctorFromPerson(personId: Long, doctorId: Long) =
        doctorDao.unlinkByIds(personId, doctorId)

    /** Returns a Flow of the given person with all their linked doctors. */
    fun getDoctorsForPerson(personId: Long): Flow<PersonWithDoctors> =
        doctorDao.getDoctorsForPerson(personId)

    /** Returns a Flow of the given doctor with all persons they are linked to. */
    fun getPersonsForDoctor(doctorId: Long): Flow<DoctorWithPersons> =
        doctorDao.getPersonsForDoctor(doctorId)

    /** Returns list of doctor IDs already linked to a person — used to pre-fill checkboxes. */
    suspend fun getLinkedDoctorIds(personId: Long): List<Long> =
        doctorDao.getLinkedDoctorIds(personId)
}
