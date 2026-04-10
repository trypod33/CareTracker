package com.caretracker.data.database

import androidx.room.*
import com.caretracker.data.models.Medication
import com.caretracker.data.models.MedicationLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications WHERE personId = :personId AND isActive = 1 ORDER BY name ASC")
    fun getMedicationsForPerson(personId: Long): Flow<List<Medication>>

    @Query("SELECT * FROM medications WHERE isActive = 1 ORDER BY personId, name ASC")
    fun getAllActiveMedications(): Flow<List<Medication>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Long): Medication?

    @Query("SELECT * FROM medications WHERE pillsRemaining <= refillReminderAt AND isActive = 1 ORDER BY pillsRemaining ASC")
    fun getMedicationsNeedingRefill(): Flow<List<Medication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medication: Medication): Long

    @Update
    suspend fun update(medication: Medication)

    @Delete
    suspend fun delete(medication: Medication)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: MedicationLog): Long

    @Query("SELECT * FROM medication_logs WHERE medicationId = :medicationId ORDER BY takenAt DESC LIMIT 30")
    fun getLogsForMedication(medicationId: Long): Flow<List<MedicationLog>>

    @Query("SELECT * FROM medication_logs WHERE personId = :personId AND takenAt BETWEEN :startMs AND :endMs ORDER BY takenAt ASC")
    suspend fun getLogsForPersonInTimeRange(personId: Long, startMs: Long, endMs: Long): List<MedicationLog>

    @Query("SELECT * FROM medication_logs WHERE medicationId = :medicationId AND takenAt BETWEEN :startMs AND :endMs ORDER BY takenAt ASC")
    suspend fun getLogsForMedicationInTimeRange(medicationId: Long, startMs: Long, endMs: Long): List<MedicationLog>
}
