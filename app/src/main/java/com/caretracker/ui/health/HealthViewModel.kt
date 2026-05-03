package com.caretracker.ui.health

import androidx.lifecycle.*
import com.caretracker.data.entities.HealthEntryEntity
import com.caretracker.data.repository.CareTrackerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HealthViewModel(private val repository: CareTrackerRepository) : ViewModel() {

    private val _entries = MutableStateFlow<List<HealthEntryEntity>>(emptyList())
    val entries: StateFlow<List<HealthEntryEntity>> = _entries.asStateFlow()

    private val _todayEntry = MutableStateFlow<HealthEntryEntity?>(null)
    val todayEntry: StateFlow<HealthEntryEntity?> = _todayEntry.asStateFlow()

    private var currentUserId: Long = -1L

    fun loadForUser(userId: Long) {
        currentUserId = userId
        viewModelScope.launch {
            repository.getRecentHealthEntries(userId).collect { list ->
                _entries.value = list
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                _todayEntry.value = list.firstOrNull { it.entryDate == today }
            }
        }
    }

    fun saveEntry(newEntry: HealthEntryEntity) {
        viewModelScope.launch {
            val existing = repository.getHealthEntryForDate(currentUserId, newEntry.entryDate)
            when {
                newEntry.id != 0L && existing?.id == newEntry.id -> {
                    repository.updateHealthEntry(newEntry)
                }
                existing != null -> {
                    val oldNote = existing.notes.orEmpty().trim()
                    val newNote = newEntry.notes.orEmpty().trim()
                    val mergedNotes: String? = when {
                        oldNote.isBlank() && newNote.isBlank() -> null
                        oldNote.isBlank() -> newNote
                        newNote.isBlank() -> oldNote
                        else -> oldNote + "\n...\n" + newNote
                    }
                    val merged = existing.copy(
                        steps           = (existing.steps ?: 0) + (newEntry.steps ?: 0),
                        calories        = (existing.calories ?: 0) + (newEntry.calories ?: 0),
                        waterOz         = (existing.waterOz ?: 0f) + (newEntry.waterOz ?: 0f),
                        exerciseMinutes = (existing.exerciseMinutes ?: 0) + (newEntry.exerciseMinutes ?: 0),
                        weight          = newEntry.weight ?: existing.weight,
                        weightUnit      = if (newEntry.weight != null) newEntry.weightUnit else existing.weightUnit,
                        heartRate       = newEntry.heartRate ?: existing.heartRate,
                        bloodPressureSystolic  = newEntry.bloodPressureSystolic ?: existing.bloodPressureSystolic,
                        bloodPressureDiastolic = newEntry.bloodPressureDiastolic ?: existing.bloodPressureDiastolic,
                        bloodSugar      = newEntry.bloodSugar ?: existing.bloodSugar,
                        sleepHours      = newEntry.sleepHours ?: existing.sleepHours,
                        sleepQuality    = newEntry.sleepQuality ?: existing.sleepQuality,
                        mood            = newEntry.mood ?: existing.mood,
                        energy          = newEntry.energy ?: existing.energy,
                        notes           = mergedNotes
                    )
                    repository.updateHealthEntry(merged)
                }
                else -> {
                    repository.insertHealthEntry(newEntry)
                }
            }
        }
    }

    fun deleteEntry(entry: HealthEntryEntity) {
        viewModelScope.launch { repository.deleteHealthEntry(entry) }
    }

    class Factory(private val r: CareTrackerRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(c: Class<T>): T = HealthViewModel(r) as T
    }
}
