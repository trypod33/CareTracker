package com.caretracker.data.repository

import com.caretracker.data.database.DoctorDao
import com.caretracker.data.models.Doctor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DoctorRepository @Inject constructor(private val doctorDao: DoctorDao) {
    val allDoctors: Flow<List<Doctor>> = doctorDao.getAllDoctors()
    suspend fun insertDoctor(doctor: Doctor) = doctorDao.insertDoctor(doctor)
    suspend fun updateDoctor(doctor: Doctor) = doctorDao.updateDoctor(doctor)
    suspend fun deleteDoctor(doctor: Doctor) = doctorDao.deleteDoctor(doctor)
}
