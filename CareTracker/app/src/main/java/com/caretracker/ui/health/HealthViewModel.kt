package com.caretracker.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.DailyHealthEntry
import com.caretracker.data.repository.HealthRepository
import com.caretracker.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val repository: HealthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    // Mirror SessionManager so the list auto-refreshes on person switch
    @OptIn(ExperimentalCoroutinesApi::class)
    val entries: StateFlow<List<DailyHealthEntry>> =
        sessionManager.activePersonIdFlow
            .flatMapLatest { pid ->
                if (pid == -1L) flowOf(emptyList())
                else repository.getAllForPerson(pid)
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun save(entry: DailyHealthEntry) {
        viewModelScope.launch { repository.save(entry) }
    }

    fun delete(entry: DailyHealthEntry) {
        viewModelScope.launch { repository.delete(entry) }
    }

    suspend fun getById(id: Long): DailyHealthEntry? = repository.getById(id)

    suspend fun getEntryForDate(date: String): DailyHealthEntry? {
        val pid = sessionManager.activePersonId
        return if (pid == -1L) null else repository.getEntryForDate(pid, date)
    }
}
