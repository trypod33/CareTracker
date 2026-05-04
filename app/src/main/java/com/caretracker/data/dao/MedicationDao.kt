package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.entities.MedicationEntity
import com.caretracker.data.entities.MedLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications WHERE isActive = 1 ORDER BY userId, name")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE userId = :userId AND isActive = 1")
    fun getMedicationsForUser(userId: Long): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Long): MedicationEntity?

    @Query("SELECT * FROM med_logs WHERE medicationId = :medId AND takenDate = :date")
    fun getLogsForDate(medId: Long, date: String): Flow<List<MedLogEntity>>

    @Query("SELECT * FROM med_logs WHERE medicationId = :medId ORDER BY takenAt DESC LIMIT 30")
    fun getRecentLogs(medId: Long): Flow<List<MedLogEntity>>
    @Query("SELECT * FROM med_logs WHERE medicationId = :medId AND takenDate = :date")
    suspend fun getMedLogsForDateOnce(medId: Long, date: String): List<MedLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(med: MedicationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MedLogEntity): Long

    @Update
    suspend fun updateMedication(med: MedicationEntity)

    @Delete
    suspend fun deleteMedication(med: MedicationEntity)
}
