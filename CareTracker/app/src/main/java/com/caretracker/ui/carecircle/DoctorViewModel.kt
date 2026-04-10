package com.caretracker.ui.carecircle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.Doctor
import com.caretracker.data.models.PersonDoctorCrossRef
import com.caretracker.data.repository.DoctorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val repository: DoctorRepository
) : ViewModel() {

    // repository.getAllDoctors() matches DoctorRepository fun getAllDoctors()
    val allDoctors: StateFlow<List<Doctor>> = repository.getAllDoctors()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveDoctor(doctor: Doctor) {
        viewModelScope.launch {
            if (doctor.id == 0L) repository.insertDoctor(doctor)
            else repository.updateDoctor(doctor)
        }
    }

    fun deleteDoctor(doctor: Doctor) {
        viewModelScope.launch { repository.deleteDoctor(doctor) }
    }

    fun linkDoctorToPerson(personId: Long, doctorId: Long) {
        viewModelScope.launch { repository.linkDoctorToPerson(personId, doctorId) }
    }

    fun unlinkDoctorFromPerson(personId: Long, doctorId: Long) {
        viewModelScope.launch { repository.unlinkDoctorFromPerson(personId, doctorId) }
    }
}
