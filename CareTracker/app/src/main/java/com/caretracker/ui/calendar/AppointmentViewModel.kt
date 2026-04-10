package com.caretracker.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.Appointment
import com.caretracker.data.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    private val repository: AppointmentRepository
) : ViewModel() {

    val upcomingAppointments: StateFlow<List<Appointment>> =
        repository.getUpcomingAppointments(System.currentTimeMillis())
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun saveAppointment(appointment: Appointment) {
        viewModelScope.launch {
            if (appointment.id == 0L) {
                repository.insertAppointment(appointment)
            } else {
                repository.updateAppointment(appointment)
            }
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        viewModelScope.launch {
            repository.deleteAppointment(appointment)
        }
    }

    suspend fun getAppointmentById(id: Long): Appointment? =
        repository.getAppointmentById(id)
}
