package com.caretracker.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.caretracker.databinding.FragmentDashboardBinding
import com.caretracker.ui.people.PeopleListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        
        // Example of modular visibility:
        // val isDiabetic = true 
        // binding.cardHealth.visibility = if (isDiabetic) View.VISIBLE else View.GONE
    }

    private fun setupClickListeners() {
        binding.cardPeople.setOnClickListener {
            startActivity(Intent(requireContext(), PeopleListActivity::class.java))
        }
        // Add more listeners as you build out other fragments/activities
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}