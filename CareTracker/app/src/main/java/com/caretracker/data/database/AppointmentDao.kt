package com.caretracker.data.database

import androidx.room.*
import com.caretracker.data.models.Appointment
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments WHERE personId = :personId ORDER BY dateTime ASC")
    fun getAppointmentsForPerson(personId: Long): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE dateTime >= :startMs AND dateTime <= :endMs ORDER BY dateTime ASC")
    fun getAppointmentsInRange(startMs: Long, endMs: Long): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE dateTime >= :nowMs ORDER BY dateTime ASC LIMIT 10")
    fun getUpcomingAppointments(nowMs: Long): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE personId = :personId AND dateTime >= :nowMs ORDER BY dateTime ASC")
    fun getUpcomingAppointmentsForPerson(personId: Long, nowMs: Long): Flow<List<Appointment>>

    @Query("SELECT * FROM appointments WHERE id = :id")
    suspend fun getAppointmentById(id: Long): Appointment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: Appointment): Long

    @Update
    suspend fun update(appointment: Appointment)

    @Delete
    suspend fun delete(appointment: Appointment)

    @Query("DELETE FROM appointments WHERE dateTime < :thresholdMs")
    suspend fun deletePastAppointments(thresholdMs: Long)
}
