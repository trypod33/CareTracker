package com.caretracker.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.caretracker.databinding.FragmentDashboardBinding
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var userId: Long = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext().getSharedPreferences("caretracker", Context.MODE_PRIVATE)
        userId = prefs.getLong("current_user_id", -1L)

        val sdf = SimpleDateFormat("EEEE, MMMM d yyyy", Locale.getDefault())
        binding.tvDate.text = sdf.format(Date())
        binding.tvWelcome.text = "Welcome back!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
