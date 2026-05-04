package com.caretracker.ui.tasks

import androidx.lifecycle.*
import com.caretracker.data.entities.TaskEntity
import com.caretracker.data.repository.CareTrackerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: CareTrackerRepository) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> = _tasks.asStateFlow()

    private var currentUserId: Long = -1L

    fun loadForUser(userId: Long) {
        currentUserId = userId
        viewModelScope.launch {
            repository.getActiveTasks(userId).collect { list ->
                _tasks.value = list.sortedWith(compareBy(
                    { it.dueDate ?: "9999-12-31" },
                    { when (it.priority.lowercase()) { "high" -> 0; "medium" -> 1; else -> 2 } }
                ))
            }
        }
    }

    fun addTask(title: String, dueDate: String?, priority: String?) {
        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(
                    userId = currentUserId,
                    title = title,
                    dueDate = dueDate,
                    priority = priority ?: "medium",
                    status = "todo"
                )
            )
        }
    }

    fun toggleComplete(task: TaskEntity) {
        viewModelScope.launch {
            val newStatus = if (task.status == "done") "todo" else "done"
            repository.updateTask(task.copy(
                status = newStatus,
                completedAt = if (newStatus == "done") System.currentTimeMillis() else null
            ))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun getCurrentUserId() = currentUserId

    class Factory(private val r: CareTrackerRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(c: Class<T>): T = TasksViewModel(r) as T
    }
}
