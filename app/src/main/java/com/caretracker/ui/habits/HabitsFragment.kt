package com.caretracker.ui.habits

import android.content.Context
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
import kotlinx.coroutines.launch

class HabitsFragment : Fragment() {
    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!
    private var userId: Long = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("caretracker", Context.MODE_PRIVATE)
        userId = prefs.getLong("current_user_id", -1L)
        val repo = (requireActivity().application as CareTrackerApp).repository

        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            repo.getHabitsForUser(userId).collect { habits ->
                binding.rvHabits.adapter = HabitAdapter(habits) { habit ->
                    logHabit(habit)
                }
            }
        }

        binding.fabAddHabit.setOnClickListener {
            showAddHabitDialog()
        }
    }

    private fun logHabit(habit: HabitEntity) {
        val repo = (requireActivity().application as CareTrackerApp).repository
        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
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

    private fun showAddHabitDialog() {
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
