package com.caretracker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.caretracker.CareTrackerApp
import com.caretracker.databinding.FragmentDashboardBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("EEEE, MMMM d yyyy", Locale.getDefault())
        binding.tvDate.text = sdf.format(Date())

        val app = requireActivity().application as CareTrackerApp

        viewLifecycleOwner.lifecycleScope.launch {
            app.currentUserIdFlow.collect { userId ->
                val user = app.repository.getUserById(userId)
                val name = user?.displayName ?: user?.username ?: "there"
                binding.tvWelcome.text = "Welcome, $name!"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
