package com.caretracker.ui.medications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.Medication
import com.caretracker.data.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val repository: MedicationRepository
) : ViewModel() {

    val allMedications: StateFlow<List<Medication>> = repository.allActiveMedications
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveMedication(medication: Medication) {
        viewModelScope.launch {
            if (medication.id == 0L) {
                repository.insertMedication(medication)
            } else {
                repository.updateMedication(medication)
            }
        }
    }

    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            repository.deleteMedication(medication)
        }
    }

    suspend fun getMedicationById(id: Long): Medication? =
        repository.getMedicationById(id)
}
