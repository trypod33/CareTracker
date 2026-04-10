package com.caretracker.data.repository

import com.caretracker.data.database.MedicationDao
import com.caretracker.data.models.Medication
import com.caretracker.data.models.MedicationLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepository @Inject constructor(
    private val medicationDao: MedicationDao
) {
    val allActiveMedications: Flow<List<Medication>> = medicationDao.getAllActiveMedications()

    fun getMedicationsForPerson(personId: Long): Flow<List<Medication>> =
        medicationDao.getMedicationsForPerson(personId)

    suspend fun getMedicationById(id: Long): Medication? =
        medicationDao.getMedicationById(id)

    suspend fun insertMedication(medication: Medication): Long =
        medicationDao.insert(medication)

    suspend fun updateMedication(medication: Medication) =
        medicationDao.update(medication)

    suspend fun deleteMedication(medication: Medication) =
        medicationDao.delete(medication)

    suspend fun logMedication(log: MedicationLog): Long =
        medicationDao.insertLog(log)

    fun getMedicationsNeedingRefill(): Flow<List<Medication>> =
        medicationDao.getMedicationsNeedingRefill()
}
