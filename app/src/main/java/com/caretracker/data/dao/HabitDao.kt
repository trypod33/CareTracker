package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.entities.HabitEntity
import com.caretracker.data.entities.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE userId = :userId AND isActive = 1")
    fun getHabitsForUser(userId: Long): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND loggedDate = :date")
    suspend fun getLogForDate(habitId: Long, date: String): HabitLogEntity?

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY loggedDate DESC LIMIT 7")
    fun getRecentLogs(habitId: Long): Flow<List<HabitLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLogEntity): Long

    @Update
    suspend fun updateHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteHabit(habit: HabitEntity)

    @Delete
    suspend fun deleteLog(log: HabitLogEntity)
}
