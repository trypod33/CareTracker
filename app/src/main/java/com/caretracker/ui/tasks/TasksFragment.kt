package com.caretracker.ui.tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.CareTrackerApp
import com.caretracker.data.entities.TaskEntity
import com.caretracker.databinding.FragmentTasksBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val vm: TasksViewModel by viewModels {
        TasksViewModel.Factory((requireActivity().application as CareTrackerApp).repository)
    }

    private lateinit var adapter: TaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as CareTrackerApp

        adapter = TaskAdapter(
            onToggle = { vm.toggleComplete(requireContext(), it) },
            onDelete = { confirmDelete(it) },
            onEdit = { showEditTaskDialog(it) }
        )

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            app.currentUserIdFlow.collect { userId ->
                vm.loadForUser(userId)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.tasks.collect { list ->
                adapter.submitList(list)
                binding.tvTasksEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.fabAddTask.setOnClickListener { showAddTaskDialog() }

        if (arguments?.getBoolean("openAddDialog", false) == true) {
            binding.root.post { showAddTaskDialog() }
        }
    }

    private fun showAddTaskDialog() {
        var selectedDate: String? = null
        var reminderAtMillis: Long? = null

        val etTitle = EditText(requireContext()).apply { hint = "Task title" }

        val etDueDate = EditText(requireContext()).apply {
            hint = "Due date (tap to pick)"
            isFocusable = false
            isClickable = true
            setOnClickListener {
                val cal = Calendar.getInstance()
                DatePickerDialog(requireContext(), { _, y, m, d ->
                    selectedDate = "%04d-%02d-%02d".format(y, m + 1, d)
                    setText(selectedDate)
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        val spinnerPriority = Spinner(requireContext()).apply {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOf("medium", "high", "low")
            )
        }

        val cbReminder = CheckBox(requireContext()).apply {
            text = "Set reminder"
        }

        val tvReminder = TextView(requireContext()).apply {
            text = "No reminder selected"
            textSize = 12f
        }

        tvReminder.setOnClickListener {
            if (!cbReminder.isChecked) return@setOnClickListener
            pickReminderDateTime { millis ->
                reminderAtMillis = millis
                tvReminder.text = "Reminder: " + formatReminder(millis)
            }
        }

        cbReminder.setOnCheckedChangeListener { _, checked ->
            if (checked && reminderAtMillis == null) {
                tvReminder.performClick()
            } else if (!checked) {
                reminderAtMillis = null
                tvReminder.text = "No reminder selected"
            }
        }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 16, 48, 8)
            addView(etTitle)
            addView(etDueDate)
            addView(spinnerPriority)
            addView(cbReminder)
            addView(tvReminder)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Add Task")
            .setView(container)
            .setPositiveButton("Add") { _, _ ->
                val title = etTitle.text.toString().trim()
                if (title.isNotEmpty()) {
                    vm.addTask(
                        requireContext(),
                        title,
                        selectedDate,
                        spinnerPriority.selectedItem.toString(),
                        cbReminder.isChecked,
                        reminderAtMillis
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditTaskDialog(task: TaskEntity) {
        var selectedDate: String? = task.dueDate
        var reminderAtMillis: Long? = task.reminderAtMillis

        val etTitle = EditText(requireContext()).apply {
            hint = "Task title"
            setText(task.title)
        }

        val etDueDate = EditText(requireContext()).apply {
            hint = "Due date (tap to pick)"
            isFocusable = false
            isClickable = true
            setText(task.dueDate ?: "")
            setOnClickListener {
                val cal = Calendar.getInstance()
                DatePickerDialog(requireContext(), { _, y, m, d ->
                    selectedDate = "%04d-%02d-%02d".format(y, m + 1, d)
                    setText(selectedDate)
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
            setOnLongClickListener {
                selectedDate = null
                setText("")
                true
            }
        }

        val priorities = listOf("medium", "high", "low")
        val spinnerPriority = Spinner(requireContext()).apply {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                priorities
            )
            setSelection(priorities.indexOf(task.priority.lowercase()).coerceAtLeast(0))
        }

        val cbReminder = CheckBox(requireContext()).apply {
            text = "Set reminder"
            isChecked = task.reminderEnabled
        }

        val tvReminder = TextView(requireContext()).apply {
            text = task.reminderAtMillis?.let { "Reminder: " + formatReminder(it) } ?: "No reminder selected"
            textSize = 12f
        }

        tvReminder.setOnClickListener {
            if (!cbReminder.isChecked) return@setOnClickListener
            pickReminderDateTime { millis ->
                reminderAtMillis = millis
                tvReminder.text = "Reminder: " + formatReminder(millis)
            }
        }

        cbReminder.setOnCheckedChangeListener { _, checked ->
            if (checked && reminderAtMillis == null) {
                tvReminder.performClick()
            } else if (!checked) {
                reminderAtMillis = null
                tvReminder.text = "No reminder selected"
            }
        }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 16, 48, 8)
            addView(etTitle)
            addView(etDueDate)
            addView(spinnerPriority)
            addView(cbReminder)
            addView(tvReminder)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Task")
            .setView(container)
            .setPositiveButton("Save") { _, _ ->
                val title = etTitle.text.toString().trim()
                if (title.isNotEmpty()) {
                    vm.updateTask(
                        requireContext(),
                        task,
                        title,
                        selectedDate,
                        spinnerPriority.selectedItem.toString(),
                        cbReminder.isChecked,
                        reminderAtMillis
                    )
                }
            }
            .setNeutralButton("Clear Reminder") { _, _ ->
                val title = etTitle.text.toString().trim()
                if (title.isNotEmpty()) {
                    vm.updateTask(
                        requireContext(),
                        task,
                        title,
                        selectedDate,
                        spinnerPriority.selectedItem.toString(),
                        false,
                        null
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDelete(task: TaskEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete task?")
            .setMessage(task.title + " will be removed.")
            .setPositiveButton("Delete") { _, _ -> vm.deleteTask(requireContext(), task) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun pickReminderDateTime(onSelected: (Long) -> Unit) {
        val cal = Calendar.getInstance()
        DatePickerDialog(requireContext(), { _, y, m, d ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val result = Calendar.getInstance().apply {
                    set(Calendar.YEAR, y)
                    set(Calendar.MONTH, m)
                    set(Calendar.DAY_OF_MONTH, d)
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onSelected(result.timeInMillis)
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun formatReminder(millis: Long): String {
        return SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()).format(Date(millis))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
