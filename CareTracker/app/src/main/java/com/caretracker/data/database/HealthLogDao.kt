package com.caretracker.data.database

import androidx.room.*
import com.caretracker.data.models.HealthLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthLogDao {

    @Query("SELECT * FROM health_logs WHERE personId = :personId AND metricId = :metricId ORDER BY timestamp DESC LIMIT 30")
    fun getLogsForPersonByType(personId: Long, metricId: Long): Flow<List<HealthLog>>

    @Query("SELECT * FROM health_logs WHERE personId = :personId ORDER BY timestamp DESC LIMIT 50")
    fun getAllLogsForPerson(personId: Long): Flow<List<HealthLog>>

    @Query("SELECT * FROM health_logs WHERE personId = :personId AND timestamp BETWEEN :startOfDayMs AND :endOfDayMs")
    suspend fun getLogsForPersonOnDate(personId: Long, startOfDayMs: Long, endOfDayMs: Long): List<HealthLog>

    @Query("SELECT * FROM health_logs WHERE personId = :personId AND timestamp BETWEEN :startOfDayMs AND :endOfDayMs ORDER BY timestamp ASC")
    suspend fun getLogsForPersonOnDateRange(personId: Long, startOfDayMs: Long, endOfDayMs: Long): List<HealthLog>

    @Transaction
    @Query("SELECT * FROM health_logs WHERE personId = :personId AND metricId = :metricId AND timestamp BETWEEN :startMs AND :endMs ORDER BY timestamp ASC")
    suspend fun getLogsInRange(personId: Long, metricId: Long, startMs: Long, endMs: Long): List<HealthLog>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: HealthLog): Long

    @Update
    suspend fun update(log: HealthLog)

    @Delete
    suspend fun delete(log: HealthLog)
}
