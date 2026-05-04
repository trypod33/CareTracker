package com.caretracker.ui.habits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.CareTrackerApp
import com.caretracker.databinding.FragmentHabitsBinding
import com.caretracker.data.entities.HabitEntity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitsFragment : Fragment() {
    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!
    private var habitsJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as CareTrackerApp

        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())

        // Observe user switches — cancel previous collection, start new one
        viewLifecycleOwner.lifecycleScope.launch {
            app.currentUserIdFlow.collect { userId ->
                habitsJob?.cancel()
                habitsJob = launch {
                    app.repository.getHabitsForUser(userId).collect { habits ->
                        binding.rvHabits.adapter = HabitAdapter(habits) { habit ->
                            logHabit(habit)
                        }
                    }
                }
            }
        }

        binding.fabAddHabit.setOnClickListener {
            showAddHabitDialog(app.currentUserId)
        }
    }

    private fun logHabit(habit: HabitEntity) {
        val repo = (requireActivity().application as CareTrackerApp).repository
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        lifecycleScope.launch {
            val existing = repo.getHabitLogForDate(habit.id, today)
            if (existing == null) {
                repo.insertHabitLog(
                    com.caretracker.data.entities.HabitLogEntity(
                        habitId = habit.id,
                        loggedDate = today
                    )
                )
            } else {
                repo.deleteHabitLog(existing)
            }
        }
    }

    private fun showAddHabitDialog(userId: Long) {
        val input = TextInputEditText(requireContext())
        input.hint = "Habit name"
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Habit")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    val repo = (requireActivity().application as CareTrackerApp).repository
                    lifecycleScope.launch {
                        repo.insertHabit(HabitEntity(userId = userId, name = name))
                    }
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
