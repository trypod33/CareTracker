package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.entities.BloodPressureReadingEntity

@Dao
interface BloodPressureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reading: BloodPressureReadingEntity): Long

    @Query("SELECT * FROM blood_pressure_readings WHERE entryId = :entryId ORDER BY readingTime ASC")
    suspend fun getReadingsForEntry(entryId: Long): List<BloodPressureReadingEntity>

    @Delete
    suspend fun delete(reading: BloodPressureReadingEntity)

    @Query("DELETE FROM blood_pressure_readings WHERE entryId = :entryId")
    suspend fun deleteAllForEntry(entryId: Long)
}
