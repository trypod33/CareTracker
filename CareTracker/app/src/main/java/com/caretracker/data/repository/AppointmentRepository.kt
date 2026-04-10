package com.caretracker.data.repository

import com.caretracker.data.database.AppointmentDao
import com.caretracker.data.models.Appointment
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentRepository @Inject constructor(
    private val appointmentDao: AppointmentDao
) {
    fun getUpcomingAppointments(nowMs: Long): Flow<List<Appointment>> =
        appointmentDao.getUpcomingAppointments(nowMs)

    fun getAppointmentsForPerson(personId: Long): Flow<List<Appointment>> =
        appointmentDao.getAppointmentsForPerson(personId)

    fun getAppointmentsInRange(startMs: Long, endMs: Long): Flow<List<Appointment>> =
        appointmentDao.getAppointmentsInRange(startMs, endMs)

    suspend fun getAppointmentById(id: Long): Appointment? =
        appointmentDao.getAppointmentById(id)

    suspend fun insertAppointment(appointment: Appointment): Long =
        appointmentDao.insert(appointment)

    suspend fun updateAppointment(appointment: Appointment) =
        appointmentDao.update(appointment)

    suspend fun deleteAppointment(appointment: Appointment) =
        appointmentDao.delete(appointment)
}
