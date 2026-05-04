package com.caretracker.ui.tasks

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.CareTrackerApp
import com.caretracker.data.entities.TaskEntity
import com.caretracker.databinding.FragmentTasksBinding
import kotlinx.coroutines.launch
import java.util.Calendar

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
            onToggle = { vm.toggleComplete(it) },
            onDelete = { confirmDelete(it) }
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
                binding.tvTasksEmpty.visibility =
                    if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.fabAddTask.setOnClickListener { showAddTaskDialog() }
    }

    private fun showAddTaskDialog() {
        var selectedDate: String? = null

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
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                   cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
        val spinnerPriority = Spinner(requireContext()).apply {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                listOf("medium", "high", "low")
            )
        }

        val container = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 16, 48, 8)
            addView(etTitle)
            addView(etDueDate)
            addView(spinnerPriority)
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Add Task")
            .setView(container)
            .setPositiveButton("Add") { _, _ ->
                val title = etTitle.text.toString().trim()
                if (title.isNotEmpty()) {
                    vm.addTask(title, selectedDate, spinnerPriority.selectedItem.toString())
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDelete(task: TaskEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete task?")
            .setMessage(task.title + " will be removed.")
            .setPositiveButton("Delete") { _, _ -> vm.deleteTask(task) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
