package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.models.DailyHealthEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyHealthEntryDao {

    @Query("SELECT * FROM daily_health_entries WHERE personId = :personId ORDER BY date DESC")
    fun getAllForPerson(personId: Long): Flow<List<DailyHealthEntry>>

    @Query("SELECT * FROM daily_health_entries WHERE personId = :personId AND date = :date LIMIT 1")
    suspend fun getEntryForDate(personId: Long, date: String): DailyHealthEntry?

    @Query("SELECT * FROM daily_health_entries WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): DailyHealthEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: DailyHealthEntry): Long

    @Update
    suspend fun update(entry: DailyHealthEntry)

    @Delete
    suspend fun delete(entry: DailyHealthEntry)
}
