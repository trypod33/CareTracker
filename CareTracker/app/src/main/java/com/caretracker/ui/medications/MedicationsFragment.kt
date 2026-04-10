package com.caretracker.ui.medications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.databinding.FragmentMedicationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MedicationsFragment : Fragment() {
    private var _binding: FragmentMedicationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MedicationViewModel by viewModels()
    private lateinit var adapter: MedicationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MedicationAdapter { med ->
            val intent = Intent(requireContext(), AddEditMedActivity::class.java).apply {
                putExtra("MED_ID", med.id)
            }
            startActivity(intent)
        }

        binding.rvMedications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMedications.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allMedications.collect { meds ->
                adapter.submitList(meds)
            }
        }

        binding.fabAddMed.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditMedActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
