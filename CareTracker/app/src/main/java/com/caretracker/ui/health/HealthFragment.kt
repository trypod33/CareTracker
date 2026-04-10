package com.caretracker.ui.health

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.databinding.FragmentHealthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HealthFragment : Fragment() {
    private var _binding: FragmentHealthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HealthViewModel by viewModels()
    private lateinit var adapter: HealthLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HealthLogAdapter { log ->
            val intent = Intent(requireContext(), HealthLogActivity::class.java).apply {
                putExtra("LOG_ID", log.id)
            }
            startActivity(intent)
        }

        binding.rvHealthLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHealthLogs.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.healthLogs.collect { logs ->
                adapter.submitList(logs)
            }
        }

        binding.fabAddHealthLog.setOnClickListener {
            startActivity(Intent(requireContext(), HealthLogActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
