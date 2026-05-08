package com.caretracker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.caretracker.CareTrackerApp
import com.caretracker.R
import com.caretracker.databinding.FragmentDashboardBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var snapshotJob: Job? = null

    private val today: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()

        val app = requireActivity().application as CareTrackerApp
        snapshotJob?.cancel()
        snapshotJob = viewLifecycleOwner.lifecycleScope.launch {
            app.currentUserIdFlow.collect { userId ->
                loadSnapshot(app, userId)
            }
        }
    }

    private fun setupNavigation() {
        binding.cardHabits.setOnClickListener { navigateAsTopLevel(R.id.habitsFragment) }
        binding.cardMeds.setOnClickListener { navigateAsTopLevel(R.id.medicationsFragment) }
        binding.cardTasks.setOnClickListener { navigateAsTopLevel(R.id.tasksFragment) }
        binding.cardMood.setOnClickListener { navigateAsTopLevel(R.id.healthFragment) }
        binding.cardWater.setOnClickListener { navigateAsTopLevel(R.id.healthFragment) }
        binding.btnViewAllHabits.setOnClickListener { navigateAsTopLevel(R.id.habitsFragment) }
        binding.btnViewAllMeds.setOnClickListener { navigateAsTopLevel(R.id.medicationsFragment) }
        binding.btnViewAllTasks.setOnClickListener { navigateAsTopLevel(R.id.tasksFragment) }

        binding.btnAddHabit.setOnClickListener {
            navigateAsTopLevelWithArgs(R.id.habitsFragment, bundleOf("openAddDialog" to true))
        }

        binding.btnAddTask.setOnClickListener {
            navigateAsTopLevelWithArgs(R.id.tasksFragment, bundleOf("openAddDialog" to true))
        }

        binding.btnAddWater.setOnClickListener {
            val app = requireActivity().application as CareTrackerApp
            val oz = binding.etBottleSize.text?.toString()?.trim()?.toFloatOrNull()

            if (oz == null || oz <= 0f) {
                Toast.makeText(requireContext(), "Enter a valid bottle size", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val userId = app.currentUserId
                app.repository.addWaterOz(userId, today, oz)
                val total = app.repository.getWaterOzForDate(userId, today)
                binding.tvWaterOz.text = formatOz(total)
                Toast.makeText(requireContext(), "Added ${formatOz(oz)} oz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateAsTopLevel(destinationId: Int) {
        navigateAsTopLevelWithArgs(destinationId, null)
    }

    private fun navigateAsTopLevelWithArgs(destinationId: Int, args: Bundle?) {
        val navController = findNavController()
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .setPopUpTo(navController.graph.startDestinationId, false, true)
            .build()
        navController.navigate(destinationId, args, options)
    }

    private suspend fun loadSnapshot(app: CareTrackerApp, userId: Long) {
        val habits = app.repository.getHabitsForUser(userId).first()
        val meds = app.repository.getMedicationsForUser(userId).first()
        val tasks = app.repository.getTasksForUser(userId).first()

        var habitDone = 0
        for (habit in habits) {
            val log = app.repository.getHabitLogForDate(habit.id, today)
            val done = if (habit.targetCount > 1) {
                (log?.count ?: 0) >= habit.targetCount
            } else {
                log != null
            }
            if (done) habitDone++
        }

        var medsDone = 0
        for (med in meds) {
            if (app.repository.getMedLogsForDateOnce(med.id, today).isNotEmpty()) medsDone++
        }

        val openTasks = tasks.count { it.status != "done" }
        val moodEntry = app.repository.getMoodEntryForDate(userId, today)
        val waterOz = app.repository.getWaterOzForDate(userId, today)

        binding.tvStatHabits.text = "$habitDone/${habits.size}"
        binding.tvStatHabitsSub.text = if (habits.isNotEmpty()) "${(habitDone * 100) / habits.size}% complete" else "0% complete"
        binding.tvStatMeds.text = "$medsDone/${meds.size}"
        binding.tvStatTasks.text = openTasks.toString()
        binding.tvMoodScore.text = moodEntry?.moodScore?.toString() ?: "—"
        binding.tvMoodDate.text = moodEntry?.entryDate ?: today
        binding.tvWaterOz.text = formatOz(waterOz)
    }

    private fun formatOz(value: Float): String {
        return if (value % 1f == 0f) value.toInt().toString() else String.format(Locale.getDefault(), "%.1f", value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snapshotJob?.cancel()
        _binding = null
    }
}
