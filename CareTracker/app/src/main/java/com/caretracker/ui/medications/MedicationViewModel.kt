package com.caretracker.ui.medications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.Medication
import com.caretracker.data.repository.MedicationRepository
import com.caretracker.data.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val repository: MedicationRepository,
    private val personRepository: PersonRepository
) : ViewModel() {

    // ── Active person ───────────────────────────────────────────────────────────────
    private val _personId = MutableStateFlow(1L)
    val personId: StateFlow<Long> = _personId.asStateFlow()

    fun setPersonId(id: Long) {
        if (id != -1L) _personId.value = id
    }

    // ── Medications filtered by active person ───────────────────────────────────
    val medications: StateFlow<List<Medication>> = _personId
        .flatMapLatest { repository.getMedicationsForPerson(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── All people for person switcher chip row ────────────────────────────────
    val allPeople = personRepository.getAllPeople()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ── CRUD ──────────────────────────────────────────────────────────────────
    fun saveMedication(medication: Medication) {
        viewModelScope.launch {
            if (medication.id == 0L) repository.insertMedication(medication)
            else repository.updateMedication(medication)
        }
    }

    fun deleteMedication(medication: Medication) {
        viewModelScope.launch { repository.deleteMedication(medication) }
    }

    /**
     * Fetch a medication directly by ID without any person filter.
     * Use this in the edit screen where the ID is already known — avoids
     * the race condition of waiting for the person-filtered Flow to emit.
     */
    suspend fun getMedicationByIdDirect(id: Long): Medication? =
        repository.getMedicationById(id)

    /** Legacy alias — kept for any callers that used the old name. */
    suspend fun getMedicationById(id: Long): Medication? =
        getMedicationByIdDirect(id)
}
