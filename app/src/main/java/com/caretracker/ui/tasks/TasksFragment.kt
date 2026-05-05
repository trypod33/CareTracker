package com.caretracker.ui.tasks

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.CareTrackerApp
import com.caretracker.data.entities.TaskEntity
import com.caretracker.databinding.FragmentTasksBinding
import kotlinx.coroutines.launch

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private val vm: TasksViewModel by viewModels {
        TasksViewModel.Factory((requireActivity().application as CareTrackerApp).repository)
    }

    private lateinit var adapter: TaskAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var latestTasks: List<TaskEntity> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskAdapter(
            onToggle = { vm.toggleComplete(it) },
            onDelete = { vm.deleteTask(it) },
            onStartDrag = { holder -> itemTouchHelper.startDrag(holder) }
        )

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.adapter = adapter

        itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                persistTaskOrder()
            }

            override fun isLongPressDragEnabled(): Boolean = false
        })
        itemTouchHelper.attachToRecyclerView(binding.rvTasks)

        binding.fabAddTask.setOnClickListener { showAddTaskDialog() }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.tasks.collect { list ->
                latestTasks = list
                adapter.submitItems(list)
                binding.tvTasksEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        vm.loadForUser((requireActivity().application as CareTrackerApp).currentUserId)
    }

    private fun persistTaskOrder() {
        val reordered = adapter.currentItems()
        if (reordered.isEmpty()) return
        viewLifecycleOwner.lifecycleScope.launch {
            reordered.forEachIndexed { index, task ->
                vm.updateTask(task.copy(sortOrder = index))
            }
        }
    }

    private fun showAddTaskDialog() {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val etTitle = EditText(requireContext()).apply { hint = "Task title" }
        val etDue = EditText(requireContext()).apply { hint = "Due date (YYYY-MM-DD)" }
        val spinner = Spinner(requireContext())

        val priorities = listOf("low", "medium", "high")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, priorities)

        layout.addView(etTitle)
        layout.addView(etDue)
        layout.addView(spinner)

        AlertDialog.Builder(requireContext())
            .setTitle("Add Task")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val title = etTitle.text.toString().trim()
                if (title.isNotEmpty()) {
                    vm.addTask(
                        title = title,
                        dueDate = etDue.text.toString().trim().ifBlank { null },
                        priority = spinner.selectedItem.toString()
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
