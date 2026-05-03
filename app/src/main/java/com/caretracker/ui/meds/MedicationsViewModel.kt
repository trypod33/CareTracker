package com.caretracker.ui.meds

import androidx.lifecycle.*
import com.caretracker.data.entities.MedLogEntity
import com.caretracker.data.entities.MedicationEntity
import com.caretracker.data.repository.CareTrackerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MedWithStatus(
    val med: MedicationEntity,
    val takenToday: Boolean,
    val todayLogCount: Int
)

class MedicationsViewModel(private val repository: CareTrackerRepository) : ViewModel() {

    private val _meds = MutableStateFlow<List<MedWithStatus>>(emptyList())
    val meds: StateFlow<List<MedWithStatus>> = _meds.asStateFlow()

    private val _filter = MutableStateFlow("All")
    val filter: StateFlow<String> = _filter.asStateFlow()

    val filteredMeds: StateFlow<List<MedWithStatus>> = combine(_meds, _filter) { list, f ->
        if (f == "All") list else list.filter { it.med.prescriber == f }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filterOptions: StateFlow<List<String>> = _meds.map { list ->
        listOf("All") + list.mapNotNull { it.med.prescriber }.distinct().sorted()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("All"))

    private var currentUserId: Long = -1L

    fun loadForUser(userId: Long) {
        currentUserId = userId
        viewModelScope.launch {
            repository.getMedicationsForUser(userId).collect { medList ->
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                val withStatus = medList.map { med ->
                    val logs = repository.getMedLogsForDateOnce(med.id, today)
                    MedWithStatus(med = med, takenToday = logs.isNotEmpty(), todayLogCount = logs.size)
                }
                _meds.value = withStatus
            }
        }
    }

    fun setFilter(f: String) { _filter.value = f }

    fun takeMed(med: MedicationEntity) {
        viewModelScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            repository.insertMedLog(MedLogEntity(medicationId = med.id, takenDate = today, status = "taken"))
            repository.updateMedication(med.copy(currentCount = (med.currentCount - 1).coerceAtLeast(0)))
            loadForUser(currentUserId)
        }
    }

    fun refillMed(med: MedicationEntity) {
        viewModelScope.launch {
            repository.updateMedication(med.copy(
                currentCount = med.currentCount + (med.pillsPerRefill ?: 30),
                lastRefillDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            ))
            loadForUser(currentUserId)
        }
    }

    fun saveMed(med: MedicationEntity) {
        viewModelScope.launch {
            if (med.id == 0L) repository.insertMedication(med)
            else repository.updateMedication(med)
            loadForUser(currentUserId)
        }
    }

    fun deleteMed(med: MedicationEntity) {
        viewModelScope.launch {
            repository.deleteMedication(med)
            loadForUser(currentUserId)
        }
    }

    fun getCurrentUserId() = currentUserId

    class Factory(private val r: CareTrackerRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(c: Class<T>): T = MedicationsViewModel(r) as T
    }
}
