package com.caretracker.ui.carecircle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.Doctor
import com.caretracker.data.models.Person
import com.caretracker.data.repository.DoctorRepository
import com.caretracker.data.repository.PersonRepository
import com.caretracker.utils.SessionManager
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
    private val personRepository: PersonRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // ── Active person tracking ────────────────────────────────────────────────
    // Bug fix: was hardcoded to 1L. Now read from SessionManager so the
    // correct person's doctors are shown when the screen opens.
    private val _activePersonId = MutableStateFlow(sessionManager.activePersonId)
    val activePersonId: StateFlow<Long> = _activePersonId.asStateFlow()

    fun setActivePersonId(id: Long) { _activePersonId.value = id }

    // ── All doctors (unfiltered) ──────────────────────────────────────────────
    val allDoctors: StateFlow<List<Doctor>> = repository.getAllDoctors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── Doctors for the active person only ───────────────────────────────────
    val doctorsForActivePerson: StateFlow<List<Doctor>> = _activePersonId
        .flatMapLatest { personId ->
            if (personId == -1L) {
                // No person selected — show all
                repository.getAllDoctors()
            } else {
                repository.getDoctorsForPerson(personId).map { it.doctors }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── Doctors for a specific person (for member filter chips) ───────────────
    fun getDoctorsForPerson(personId: Long): kotlinx.coroutines.flow.Flow<List<Doctor>> =
        repository.getDoctorsForPerson(personId).map { it.doctors }

    // ── All people (for member filter chips + link checkboxes) ────────────────
    val allPeople: StateFlow<List<Person>> = personRepository.getAllPeople()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── CRUD ──────────────────────────────────────────────────────────────────

    fun saveDoctor(doctor: Doctor) {
        viewModelScope.launch {
            if (doctor.id == 0L) repository.insertDoctor(doctor)
            else repository.updateDoctor(doctor)
        }
    }

    suspend fun saveDoctorAndGetId(doctor: Doctor): Long =
        repository.insertDoctor(doctor)

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

    fun savePatientLinks(doctorId: Long, selectedPersonIds: Set<Long>, previousPersonIds: Set<Long>) {
        viewModelScope.launch {
            (selectedPersonIds - previousPersonIds).forEach { personId ->
                repository.linkDoctorToPerson(personId, doctorId)
            }
            (previousPersonIds - selectedPersonIds).forEach { personId ->
                repository.unlinkDoctorFromPerson(personId, doctorId)
            }
        }
    }

    /**
     * Returns IDs of PERSONS linked to a doctor.
     * Bug fix: previously called getLinkedDoctorIds() which returns doctor IDs,
     * not person IDs. Now calls the correct getLinkedPersonIds() query.
     */
    suspend fun getLinkedPersonIds(doctorId: Long): List<Long> =
        repository.getLinkedPersonIds(doctorId)
}
