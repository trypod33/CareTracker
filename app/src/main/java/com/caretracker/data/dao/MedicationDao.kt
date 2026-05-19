package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.entities.MedLogEntity
import com.caretracker.data.entities.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medications ORDER BY sortOrder ASC, createdAt DESC")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE userId = :userId ORDER BY sortOrder ASC, createdAt DESC")
    fun getMedicationsForUser(userId: Long): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE userId = :userId ORDER BY sortOrder ASC, createdAt DESC")
    suspend fun getMedicationsForUserOnce(userId: Long): List<MedicationEntity>

    @Query("SELECT * FROM medications WHERE id = :id LIMIT 1")
    suspend fun getMedicationById(id: Long): MedicationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity): Long

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)

    @Query("DELETE FROM medications WHERE userId = :userId")
    suspend fun deleteMedicationsByUserId(userId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MedLogEntity): Long

    @Query("SELECT * FROM med_logs WHERE medicationId = :medId AND takenDate = :date ORDER BY id DESC")
    fun getLogsForDate(medId: Long, date: String): Flow<List<MedLogEntity>>

    @Query("SELECT * FROM med_logs WHERE medicationId = :medId AND takenDate = :date ORDER BY id DESC")
    suspend fun getMedLogsForDateOnce(medId: Long, date: String): List<MedLogEntity>

    @Query("SELECT * FROM med_logs WHERE medicationId = :medId ORDER BY id DESC LIMIT 30")
    fun getRecentLogs(medId: Long): Flow<List<MedLogEntity>>

    @Query("DELETE FROM med_logs WHERE medicationId IN (SELECT id FROM medications WHERE userId = :userId)")
    suspend fun deleteLogsByUserId(userId: Long)

    @Query("SELECT ml.* FROM med_logs ml INNER JOIN medications m ON ml.medicationId = m.id WHERE m.userId = :userId")
    suspend fun getAllMedLogsForUserOnce(userId: Long): List<MedLogEntity>
}
