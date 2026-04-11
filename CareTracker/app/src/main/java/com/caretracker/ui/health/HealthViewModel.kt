package com.caretracker.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.HealthLog
import com.caretracker.data.repository.HealthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val repository: HealthRepository
) : ViewModel() {

    private val _personId = MutableStateFlow(1L)
    val personId: StateFlow<Long> = _personId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val healthLogs: StateFlow<List<HealthLog>> = _personId
        .flatMapLatest { repository.getAllLogsForPerson(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setPersonId(id: Long) { _personId.value = id }

    fun saveLog(log: HealthLog) {
        viewModelScope.launch {
            if (log.id == 0L) {
                repository.insertLog(log)
            } else {
                repository.updateLog(log)
            }
        }
    }

    fun deleteLog(log: HealthLog) {
        viewModelScope.launch {
            repository.deleteLog(log)
        }
    }
}
