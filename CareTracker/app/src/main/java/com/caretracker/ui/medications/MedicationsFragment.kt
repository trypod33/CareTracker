package com.caretracker.ui.medications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.data.models.Person
import com.caretracker.databinding.FragmentMedicationsBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MedicationsFragment : Fragment() {
    private var _binding: FragmentMedicationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MedicationViewModel by viewModels()
    private lateinit var adapter: MedicationAdapter
    private var selectedPersonId: Long = 1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MedicationAdapter(
            onClick = { med ->
                startActivity(
                    Intent(requireContext(), AddEditMedActivity::class.java)
                        .putExtra("MED_ID", med.id)
                        .putExtra("PERSON_ID", selectedPersonId)
                )
            },
            onDelete = { med -> viewModel.deleteMedication(med) }
        )

        binding.rvMedications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMedications.adapter = adapter

        // Build person switcher chips
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPeople.collect { people -> buildPersonChips(people) }
        }

        // Observe filtered medications
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.medications.collectLatest { meds ->
                adapter.submitList(meds)
                binding.tvEmptyMeds.visibility = if (meds.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.fabAddMed.setOnClickListener {
            startActivity(
                Intent(requireContext(), AddEditMedActivity::class.java)
                    .putExtra("PERSON_ID", selectedPersonId)
            )
        }
    }

    private fun buildPersonChips(people: List<Person>) {
        binding.chipGroupPeople.removeAllViews()
        people.forEach { person ->
            val chip = Chip(requireContext()).apply {
                text = "${person.avatar} ${person.name}"
                isCheckable = true
                isChecked = person.id == selectedPersonId
                setOnClickListener {
                    selectedPersonId = person.id
                    viewModel.setPersonId(person.id)
                    // Update chip checked states
                    for (i in 0 until binding.chipGroupPeople.childCount) {
                        val c = binding.chipGroupPeople.getChildAt(i) as? Chip
                        c?.isChecked = (c?.tag as? Long) == person.id
                    }
                }
                tag = person.id
            }
            binding.chipGroupPeople.addView(chip)
        }
        // Auto-select first person if none selected yet
        if (people.isNotEmpty() && !people.any { it.id == selectedPersonId }) {
            selectedPersonId = people.first().id
            viewModel.setPersonId(selectedPersonId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
