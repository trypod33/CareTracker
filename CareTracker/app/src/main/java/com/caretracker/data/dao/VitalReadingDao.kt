package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.models.VitalReading
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalReadingDao {

    // ── All readings for a person, newest first ──────────────────────────────
    @Query("SELECT * FROM vital_readings WHERE personId = :personId ORDER BY readingTimestamp DESC")
    fun getAllForPerson(personId: Long): Flow<List<VitalReading>>

    // ── By type, newest first ────────────────────────────────────────────────
    @Query("SELECT * FROM vital_readings WHERE personId = :personId AND readingType = :type ORDER BY readingTimestamp DESC")
    fun getByType(personId: Long, type: String): Flow<List<VitalReading>>

    // ── By type within a date range ──────────────────────────────────────────
    @Query("""
        SELECT * FROM vital_readings
        WHERE personId = :personId
          AND readingType = :type
          AND readingDate >= :from
          AND readingDate <= :to
        ORDER BY readingTimestamp DESC
    """)
    fun getByTypeInRange(personId: Long, type: String, from: String, to: String): Flow<List<VitalReading>>

    // ── On a specific calendar date ──────────────────────────────────────────
    @Query("SELECT * FROM vital_readings WHERE personId = :personId AND readingDate = :date ORDER BY readingTimestamp DESC")
    suspend fun getForDate(personId: Long, date: String): List<VitalReading>

    // ── Most recent N readings of a type (for dashboard summary) ─────────────
    @Query("SELECT * FROM vital_readings WHERE personId = :personId AND readingType = :type ORDER BY readingTimestamp DESC LIMIT :limit")
    suspend fun getLatest(personId: Long, type: String, limit: Int = 10): List<VitalReading>

    @Query("SELECT * FROM vital_readings WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): VitalReading?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reading: VitalReading): Long

    @Update
    suspend fun update(reading: VitalReading)

    @Delete
    suspend fun delete(reading: VitalReading)

    @Query("DELETE FROM vital_readings WHERE id = :id")
    suspend fun deleteById(id: Long)
}
