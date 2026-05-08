package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.entities.CalendarEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {
    @Query("SELECT * FROM calendar_events WHERE userId = :userId ORDER BY startDatetime ASC")
    fun getEventsForUser(userId: Long): Flow<List<CalendarEventEntity>>

    @Query("SELECT * FROM calendar_events WHERE userId = :userId AND startDatetime >= :start AND startDatetime <= :end ORDER BY startDatetime ASC")
    fun getEventsInRange(userId: Long, start: Long, end: Long): Flow<List<CalendarEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: CalendarEventEntity): Long

    @Update
    suspend fun updateEvent(event: CalendarEventEntity)

    @Delete
    suspend fun deleteEvent(event: CalendarEventEntity)

    @Query("DELETE FROM calendar_events WHERE userId = :userId")
    suspend fun deleteEventsByUserId(userId: Long)
}
