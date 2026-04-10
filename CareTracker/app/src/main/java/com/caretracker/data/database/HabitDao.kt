package com.caretracker.data.database

import androidx.room.*
import com.caretracker.data.models.Habit
import com.caretracker.data.models.HabitLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE personId = :personId AND isActive = 1 ORDER BY name ASC")
    fun getHabitsForPerson(personId: Long): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): Habit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit): Long

    @Update
    suspend fun update(habit: Habit)

    @Delete
    suspend fun delete(habit: Habit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: HabitLog): Long

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND logDate = :date LIMIT 1")
    suspend fun getLogForHabitOnDate(habitId: Long, date: String): HabitLog?

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY logDate DESC LIMIT 30")
    fun getRecentLogs(habitId: Long): Flow<List<HabitLog>>

    @Query("SELECT COUNT(*) FROM habit_logs WHERE habitId = :habitId AND logDate BETWEEN :start AND :end")
    suspend fun getStreakCount(habitId: Long, start: String, end: String): Int
}
