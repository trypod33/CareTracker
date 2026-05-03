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
    fun saveEntry(entry: HealthEntryEntity) {
        viewModelScope.launch {
            val existing = repository.getHealthEntryForDate(currentUserId, entry.entryDate)
            when {
                entry.id != 0L -> repository.updateHealthEntry(entry)
                existing != null -> repository.updateHealthEntry(entry.copy(id = existing.id))
                else -> repository.insertHealthEntry(entry)
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
