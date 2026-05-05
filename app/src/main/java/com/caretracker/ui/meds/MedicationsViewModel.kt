package com.caretracker.ui.meds

import androidx.lifecycle.*
import com.caretracker.data.entities.MedLogEntity
import com.caretracker.data.entities.MedicationEntity
import com.caretracker.data.entities.UserEntity
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

    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    private val _filter = MutableStateFlow("All")

    val users: StateFlow<List<UserEntity>> = _users.asStateFlow()
    val filter: StateFlow<String> = _filter.asStateFlow()

    val filteredMeds: StateFlow<List<MedWithStatus>> = combine(_meds, _filter, _users) { list, f, users ->
        if (f == "All") list
        else {
            val matchedUser = users.firstOrNull { it.displayName?.trim() == f || it.username == f }
            if (matchedUser != null) list.filter { it.med.userId == matchedUser.id }
            else list
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filterOptions: StateFlow<List<String>> = combine(_users, _meds) { users, meds ->
        listOf("All") + users
            .filter { u -> meds.any { m -> m.med.userId == u.id } }
            .map { u -> u.displayName?.trim()?.takeIf { it.isNotBlank() } ?: u.username }
            .distinct()
            .sorted()
    }.distinctUntilChanged().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("All"))

    private var currentUserId: Long = -1L
    private var isAdmin: Boolean = false

    fun loadForUser(userId: Long, admin: Boolean = false) {
        currentUserId = userId
        isAdmin = admin

        viewModelScope.launch {
            repository.getAllUsers().collect { users ->
                _users.value = users.filter { it.isActive }
            }
        }

        viewModelScope.launch {
            repository.getAllMedications().collect { medList ->
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                val withStatus = medList.map { med ->
                    val logs = repository.getMedLogsForDateOnce(med.id, today)
                    MedWithStatus(
                        med = med,
                        takenToday = logs.isNotEmpty(),
                        todayLogCount = logs.size
                    )
                }
                _meds.value = withStatus
            }
        }
    }

    fun nextSortOrder(): Int = (_meds.value.maxOfOrNull { it.med.sortOrder } ?: -1) + 1

    fun setFilter(f: String) {
        _filter.value = f
    }

    fun updateMedication(med: MedicationEntity) {
        viewModelScope.launch {
            repository.updateMedication(med)
        }
    }

    fun takeMed(med: MedicationEntity) {
        viewModelScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
            repository.insertMedLog(
                MedLogEntity(
                    medicationId = med.id,
                    takenDate = today,
                    status = "taken"
                )
            )
            repository.updateMedication(
                med.copy(currentCount = (med.currentCount - 1).coerceAtLeast(0))
            )
            loadForUser(currentUserId, isAdmin)
        }
    }

    fun refillMed(med: MedicationEntity) {
        viewModelScope.launch {
            repository.updateMedication(
                med.copy(
                    currentCount = med.currentCount + (med.pillsPerRefill ?: 30),
                    lastRefillDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                )
            )
            loadForUser(currentUserId, isAdmin)
        }
    }

    fun saveMed(med: MedicationEntity) {
        viewModelScope.launch {
            if (med.id == 0L) repository.insertMedication(med)
            else repository.updateMedication(med)
            loadForUser(currentUserId, isAdmin)
        }
    }

    fun deleteMed(med: MedicationEntity) {
        viewModelScope.launch {
            repository.deleteMedication(med)
            loadForUser(currentUserId, isAdmin)
        }
    }

    fun getCurrentUserId() = currentUserId

    class Factory(private val r: CareTrackerRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(c: Class<T>): T = MedicationsViewModel(r) as T
    }
}
