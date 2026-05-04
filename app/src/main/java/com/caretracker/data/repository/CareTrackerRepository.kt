package com.caretracker.data.repository

import com.caretracker.data.dao.*
import com.caretracker.data.entities.*
import kotlinx.coroutines.flow.Flow

class CareTrackerRepository(
    private val userDao: UserDao,
    private val habitDao: HabitDao,
    private val healthDao: HealthDao,
    private val medicationDao: MedicationDao,
    private val calendarDao: CalendarDao,
    private val taskDao: TaskDao,
    private val moodDao: MoodDao
) {
    // Users
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    suspend fun getUserById(id: Long) = userDao.getUserById(id)
    suspend fun getUserByUsername(username: String) = userDao.getUserByUsername(username)
    suspend fun insertUser(user: UserEntity) = userDao.insert(user)
    suspend fun updateUser(user: UserEntity) = userDao.update(user)

    // Habits
    fun getHabitsForUser(userId: Long) = habitDao.getHabitsForUser(userId)
    fun getRecentHabitLogs(habitId: Long) = habitDao.getRecentLogs(habitId)
    suspend fun getHabitLogForDate(habitId: Long, date: String) = habitDao.getLogForDate(habitId, date)
    suspend fun insertHabit(habit: HabitEntity) = habitDao.insertHabit(habit)
    suspend fun insertHabitLog(log: HabitLogEntity) = habitDao.insertLog(log)
    suspend fun updateHabit(habit: HabitEntity) = habitDao.updateHabit(habit)
    suspend fun deleteHabit(habit: HabitEntity) = habitDao.deleteHabit(habit)
    suspend fun deleteHabitLog(log: HabitLogEntity) = habitDao.deleteLog(log)

    // Health
    fun getHealthEntries(userId: Long) = healthDao.getEntriesForUser(userId)
    fun getRecentHealthEntries(userId: Long) = healthDao.getRecentEntries(userId)
    suspend fun getHealthEntryForDate(userId: Long, date: String) = healthDao.getEntryForDate(userId, date)
    suspend fun insertHealthEntry(entry: HealthEntryEntity) = healthDao.insertEntry(entry)
suspend fun deleteHealthEntry(entry: HealthEntryEntity) = healthDao.deleteEntry(entry)
    suspend fun updateHealthEntry(entry: HealthEntryEntity) = healthDao.updateEntry(entry)
    fun getVitalLogs(userId: Long) = healthDao.getVitalLogs(userId)
    suspend fun insertVitalLog(log: VitalLogEntity) = healthDao.insertVitalLog(log)

    // Medications
fun getAllMedications() = medicationDao.getAllMedications()
    fun getMedicationsForUser(userId: Long) = medicationDao.getMedicationsForUser(userId)
    suspend fun getMedicationById(id: Long) = medicationDao.getMedicationById(id)
    fun getMedLogsForDate(medId: Long, date: String) = medicationDao.getLogsForDate(medId, date)
    fun getRecentMedLogs(medId: Long) = medicationDao.getRecentLogs(medId)
    suspend fun insertMedication(med: MedicationEntity) = medicationDao.insertMedication(med)
    suspend fun insertMedLog(log: MedLogEntity) = medicationDao.insertLog(log)
    suspend fun updateMedication(med: MedicationEntity) = medicationDao.updateMedication(med)
    suspend fun deleteMedication(med: MedicationEntity) = medicationDao.deleteMedication(med)
    suspend fun getMedLogsForDateOnce(medId: Long, date: String) = medicationDao.getMedLogsForDateOnce(medId, date)

    // Calendar
    fun getEventsForUser(userId: Long) = calendarDao.getEventsForUser(userId)
    fun getEventsInRange(userId: Long, start: Long, end: Long) = calendarDao.getEventsInRange(userId, start, end)
    suspend fun insertEvent(event: CalendarEventEntity) = calendarDao.insertEvent(event)
    suspend fun updateEvent(event: CalendarEventEntity) = calendarDao.updateEvent(event)
    suspend fun deleteEvent(event: CalendarEventEntity) = calendarDao.deleteEvent(event)

    // Tasks
    fun getTasksForUser(userId: Long) = taskDao.getTasksForUser(userId)
    fun getActiveTasks(userId: Long) = taskDao.getActiveTasks(userId)
    suspend fun insertTask(task: TaskEntity) = taskDao.insertTask(task)
    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)
    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

    // Mood
    fun getMoodEntries(userId: Long) = moodDao.getMoodEntriesForUser(userId)
    suspend fun insertMoodEntry(entry: MoodJournalEntity) = moodDao.insertEntry(entry)
    suspend fun updateMoodEntry(entry: MoodJournalEntity) = moodDao.updateEntry(entry)
    suspend fun deleteMoodEntry(entry: MoodJournalEntity) = moodDao.deleteEntry(entry)
}
