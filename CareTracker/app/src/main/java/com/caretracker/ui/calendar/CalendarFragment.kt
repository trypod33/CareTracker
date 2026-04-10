package com.caretracker.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.databinding.FragmentCalendarBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalendarFragment : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppointmentViewModel by viewModels()
    private lateinit var adapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AppointmentAdapter { appt ->
            val intent = Intent(requireContext(), AddEditAppointmentActivity::class.java).apply {
                putExtra("APPOINTMENT_ID", appt.id)
            }
            startActivity(intent)
        }

        binding.rvAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAppointments.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.upcomingAppointments.collect { appts ->
                adapter.submitList(appts)
            }
        }

        binding.fabAddAppointment.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditAppointmentActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
