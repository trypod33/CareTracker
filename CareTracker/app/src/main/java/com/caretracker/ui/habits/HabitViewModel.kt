package com.caretracker.ui.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.Habit
import com.caretracker.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    // Show habits for personId=1 by default; can be filtered later
    private val _personId = MutableStateFlow(1L)
    val personId: StateFlow<Long> = _personId.asStateFlow()

    val habits: StateFlow<List<Habit>> = _personId
        .flatMapLatest { repository.getHabitsForPerson(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setPersonId(id: Long) { _personId.value = id }

    fun saveHabit(habit: Habit) {
        viewModelScope.launch {
            if (habit.id == 0L) {
                repository.insertHabit(habit)
            } else {
                repository.updateHabit(habit)
            }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repository.deleteHabit(habit)
        }
    }

    suspend fun getHabitById(id: Long): Habit? =
        repository.getHabitById(id)
}
