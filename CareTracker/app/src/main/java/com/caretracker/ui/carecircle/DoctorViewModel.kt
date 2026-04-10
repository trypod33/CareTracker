package com.caretracker.ui.carecircle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.Doctor
import com.caretracker.data.models.Person
import com.caretracker.data.models.PersonDoctorCrossRef
import com.caretracker.data.repository.DoctorRepository
import com.caretracker.data.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DoctorViewModel @Inject constructor(
    private val repository: DoctorRepository,
    private val personRepository: PersonRepository
) : ViewModel() {

    // ── Active person tracking ────────────────────────────────────────────────
    private val _activePersonId = MutableStateFlow(1L)
    val activePersonId: StateFlow<Long> = _activePersonId.asStateFlow()

    fun setActivePersonId(id: Long) { _activePersonId.value = id }

    // ── All doctors (unfiltered) ──────────────────────────────────────────────
    val allDoctors: StateFlow<List<Doctor>> = repository.getAllDoctors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── Doctors for the active person only ───────────────────────────────────
    val doctorsForActivePerson: StateFlow<List<Doctor>> = _activePersonId
        .flatMapLatest { personId ->
            repository.getDoctorsForPerson(personId)
                .map { it.doctors }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── All people (for link-to-patient checkboxes in AddEditDoctorActivity) ──
    val allPeople: StateFlow<List<Person>> = personRepository.getAllPeople()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── CRUD ──────────────────────────────────────────────────────────────────
    fun saveDoctor(doctor: Doctor) {
        viewModelScope.launch {
            if (doctor.id == 0L) repository.insertDoctor(doctor)
            else repository.updateDoctor(doctor)
        }
    }

    fun deleteDoctor(doctor: Doctor) {
        viewModelScope.launch { repository.deleteDoctor(doctor) }
    }

    // ── Linking ───────────────────────────────────────────────────────────────
    fun linkDoctorToPerson(personId: Long, doctorId: Long) {
        viewModelScope.launch { repository.linkDoctorToPerson(personId, doctorId) }
    }

    fun unlinkDoctorFromPerson(personId: Long, doctorId: Long) {
        viewModelScope.launch { repository.unlinkDoctorFromPerson(personId, doctorId) }
    }

    /** Save a complete set of patient links for a doctor, replacing previous links. */
    fun savePatientLinks(doctorId: Long, selectedPersonIds: Set<Long>, previousPersonIds: Set<Long>) {
        viewModelScope.launch {
            // Link newly selected
            (selectedPersonIds - previousPersonIds).forEach { personId ->
                repository.linkDoctorToPerson(personId, doctorId)
            }
            // Unlink deselected
            (previousPersonIds - selectedPersonIds).forEach { personId ->
                repository.unlinkDoctorFromPerson(personId, doctorId)
            }
        }
    }

    suspend fun getLinkedPersonIds(doctorId: Long): List<Long> =
        repository.getLinkedDoctorIds(doctorId)
}
