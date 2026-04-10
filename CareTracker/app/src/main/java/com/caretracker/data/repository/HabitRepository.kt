package com.caretracker.data.repository

import com.caretracker.data.database.HabitDao
import com.caretracker.data.models.Habit
import com.caretracker.data.models.HabitLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HabitRepository @Inject constructor(
    private val habitDao: HabitDao
) {
    fun getHabitsForPerson(personId: Long): Flow<List<Habit>> =
        habitDao.getHabitsForPerson(personId)

    suspend fun getHabitById(id: Long): Habit? =
        habitDao.getHabitById(id)

    suspend fun insertHabit(habit: Habit): Long =
        habitDao.insert(habit)

    suspend fun updateHabit(habit: Habit) =
        habitDao.update(habit)

    suspend fun deleteHabit(habit: Habit) =
        habitDao.delete(habit)

    suspend fun logHabit(log: HabitLog): Long =
        habitDao.insertLog(log)

    fun getRecentLogs(habitId: Long): Flow<List<HabitLog>> =
        habitDao.getRecentLogs(habitId)

    suspend fun getTodayTotal(habitId: Long, date: String): Double =
        habitDao.getTodayTotal(habitId, date)
}
