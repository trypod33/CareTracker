package com.caretracker.ui.habits

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.databinding.FragmentHabitsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HabitsFragment : Fragment() {

    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pass lifecycleScope + viewModel so adapter can launch coroutines for quick-log
        val adapter = HabitAdapter(
            scope     = viewLifecycleOwner.lifecycleScope,
            viewModel = viewModel
        ) { habit ->
            startActivity(
                Intent(requireContext(), AddEditHabitActivity::class.java)
                    .putExtra("habitId", habit.id)
            )
        }

        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabits.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.habits.collectLatest { adapter.submitList(it) }
        }

        binding.fabAddHabit.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditHabitActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
