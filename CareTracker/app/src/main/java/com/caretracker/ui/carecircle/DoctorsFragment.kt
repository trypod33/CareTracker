package com.caretracker.ui.carecircle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.databinding.FragmentDoctorsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DoctorsFragment : Fragment() {

    private var _binding: FragmentDoctorsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorViewModel by viewModels()
    private lateinit var adapter: DoctorAdapter
    private var collectJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DoctorAdapter(
            onClick = { doctor ->
                startActivity(
                    Intent(requireContext(), AddEditDoctorActivity::class.java)
                        .putExtra("DOCTOR_ID", doctor.id)
                )
            },
            onDelete = { doctor -> viewModel.deleteDoctor(doctor) }
        )

        binding.rvDoctors.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDoctors.adapter = adapter

        fun loadDoctors(showAll: Boolean) {
            collectJob?.cancel()
            collectJob = viewLifecycleOwner.lifecycleScope.launch {
                val flow = if (showAll) viewModel.allDoctors else viewModel.doctorsForActivePerson
                flow.collectLatest { doctors ->
                    adapter.submitList(doctors)
                    binding.tvEmptyDoctors.visibility =
                        if (doctors.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }

        binding.chipShowAll.setOnCheckedChangeListener { _, isChecked ->
            loadDoctors(isChecked)
        }

        loadDoctors(false)

        binding.fabAddDoctor.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditDoctorActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        collectJob?.cancel()
        _binding = null
    }
}
