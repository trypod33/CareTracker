package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.entities.HealthEntryEntity
import com.caretracker.data.entities.VitalLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthDao {
    @Query("SELECT * FROM health_entries WHERE userId = :userId ORDER BY entryDate DESC")
    fun getEntriesForUser(userId: Long): Flow<List<HealthEntryEntity>>

    @Query("SELECT * FROM health_entries WHERE userId = :userId AND entryDate = :date LIMIT 1")
    suspend fun getEntryForDate(userId: Long, date: String): HealthEntryEntity?

    @Query("SELECT * FROM health_entries WHERE userId = :userId ORDER BY entryDate DESC LIMIT 30")
    fun getRecentEntries(userId: Long): Flow<List<HealthEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: HealthEntryEntity): Long

    @Update
    suspend fun updateEntry(entry: HealthEntryEntity)

    @Delete
    suspend fun deleteEntry(entry: HealthEntryEntity)

    @Query("SELECT * FROM vital_logs WHERE userId = :userId ORDER BY recordedAt DESC")
    fun getVitalLogs(userId: Long): Flow<List<VitalLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVitalLog(log: VitalLogEntity): Long

    @Delete
    suspend fun deleteVitalLog(log: VitalLogEntity)

    @Query("SELECT * FROM health_entries WHERE userId = :userId AND bloodPressureSystolic IS NOT NULL ORDER BY entryDate DESC LIMIT 14")
    fun getRecentBPEntries(userId: Long): Flow<List<HealthEntryEntity>>

    @Query("SELECT * FROM health_entries WHERE userId = :userId AND bloodSugar IS NOT NULL ORDER BY entryDate DESC LIMIT 14")
    fun getRecentSugarEntries(userId: Long): Flow<List<HealthEntryEntity>>

    @Query("SELECT * FROM health_entries WHERE userId = :userId AND heartRate IS NOT NULL ORDER BY entryDate DESC LIMIT 14")
    fun getRecentHeartRateEntries(userId: Long): Flow<List<HealthEntryEntity>>

    @Query("SELECT * FROM health_entries WHERE userId = :userId AND sleepHours IS NOT NULL ORDER BY entryDate DESC LIMIT 14")
    fun getRecentSleepEntries(userId: Long): Flow<List<HealthEntryEntity>>

    @Query("SELECT * FROM health_entries WHERE userId = :userId AND (bloodPressureSystolic IS NOT NULL OR bloodSugar IS NOT NULL) ORDER BY entryDate DESC LIMIT 1")
    suspend fun getLatestVitalEntry(userId: Long): HealthEntryEntity?

    @Query("DELETE FROM vital_logs WHERE userId = :userId")
    suspend fun deleteVitalLogsByUserId(userId: Long)

    @Query("DELETE FROM health_entries WHERE userId = :userId")
    suspend fun deleteEntriesByUserId(userId: Long)
}
