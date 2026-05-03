package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.entities.BloodSugarReadingEntity

@Dao
interface BloodSugarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reading: BloodSugarReadingEntity): Long

    @Query("SELECT * FROM blood_sugar_readings WHERE entryId = :entryId ORDER BY readingTime ASC")
    suspend fun getReadingsForEntry(entryId: Long): List<BloodSugarReadingEntity>

    @Delete
    suspend fun delete(reading: BloodSugarReadingEntity)

    @Query("DELETE FROM blood_sugar_readings WHERE entryId = :entryId")
    suspend fun deleteAllForEntry(entryId: Long)
}
