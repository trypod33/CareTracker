package com.caretracker.ui.tasks

import android.content.Context
import androidx.lifecycle.*
import com.caretracker.data.entities.TaskEntity
import com.caretracker.data.repository.CareTrackerRepository
import com.caretracker.notifications.TaskReminderScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TasksViewModel(
    private val repository: CareTrackerRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    val tasks: StateFlow<List<TaskEntity>> = _tasks.asStateFlow()

    private var currentUserId: Long = -1L

    fun loadForUser(userId: Long) {
        currentUserId = userId
        viewModelScope.launch {
            repository.getTasksForUser(userId).collect { list ->
                _tasks.value = list.sortedWith(
                    compareBy<TaskEntity>(
                        { if (it.status == "done") 1 else 0 },
                        { if (it.dueDate.isNullOrBlank()) 0 else 1 },
                        { it.dueDate ?: "9999-12-31" },
                        {
                            when (it.priority.lowercase()) {
                                "high" -> 0
                                "medium" -> 1
                                else -> 2
                            }
                        }
                    )
                )
            }
        }
    }

    fun addTask(
        context: Context,
        title: String,
        dueDate: String?,
        priority: String?,
        reminderEnabled: Boolean,
        reminderAtMillis: Long?
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                userId = currentUserId,
                title = title,
                dueDate = dueDate,
                priority = priority ?: "medium",
                status = "todo",
                reminderEnabled = reminderEnabled,
                reminderAtMillis = if (reminderEnabled) reminderAtMillis else null
            )
            val id = repository.insertTask(task)
            val saved = task.copy(id = id)
            if (saved.reminderEnabled && saved.reminderAtMillis != null) {
                TaskReminderScheduler.schedule(context, saved)
            }
        }
    }

    fun updateTask(
        context: Context,
        task: TaskEntity,
        title: String,
        dueDate: String?,
        priority: String?,
        reminderEnabled: Boolean,
        reminderAtMillis: Long?
    ) {
        viewModelScope.launch {
            TaskReminderScheduler.cancel(context, task)

            val updated = task.copy(
                title = title,
                dueDate = dueDate,
                priority = priority ?: task.priority,
                reminderEnabled = reminderEnabled,
                reminderAtMillis = if (reminderEnabled) reminderAtMillis else null
            )

            repository.updateTask(updated)

            if (updated.reminderEnabled && updated.reminderAtMillis != null && updated.status != "done") {
                TaskReminderScheduler.schedule(context, updated)
            }
        }
    }

    fun toggleComplete(context: Context, task: TaskEntity) {
        viewModelScope.launch {
            val newStatus = if (task.status == "done") "todo" else "done"
            val updated = task.copy(
                status = newStatus,
                completedAt = if (newStatus == "done") System.currentTimeMillis() else null
            )

            repository.updateTask(updated)

            if (newStatus == "done") {
                TaskReminderScheduler.cancel(context, updated)
            } else if (updated.reminderEnabled && updated.reminderAtMillis != null) {
                TaskReminderScheduler.schedule(context, updated)
            }
        }
    }

    fun deleteTask(context: Context, task: TaskEntity) {
        viewModelScope.launch {
            TaskReminderScheduler.cancel(context, task)
            repository.deleteTask(task)
        }
    }

    fun getCurrentUserId() = currentUserId

    class Factory(private val r: CareTrackerRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(c: Class<T>): T = TasksViewModel(r) as T
    }
}
