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
import com.caretracker.data.models.Person
import com.caretracker.databinding.FragmentMedicationsBinding
import com.caretracker.utils.SessionManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MedicationsFragment : Fragment() {
    private var _binding: FragmentMedicationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MedicationViewModel by viewModels()
    private lateinit var adapter: MedicationAdapter

    @Inject lateinit var sessionManager: SessionManager

    // Seed from SessionManager; falls back to 1L if nothing saved yet
    private var selectedPersonId: Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore last active person from session
        val savedId = sessionManager.activePersonId
        if (savedId != -1L) {
            selectedPersonId = savedId
            viewModel.setPersonId(savedId)
        }

        adapter = MedicationAdapter(
            onClick = { med ->
                startActivity(
                    Intent(requireContext(), AddEditMedActivity::class.java)
                        .putExtra("MED_ID", med.id)
                        .putExtra("PERSON_ID", med.personId)
                )
            },
            onDelete = { med -> viewModel.deleteMedication(med) }
        )

        binding.rvMedications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMedications.adapter = adapter

        // Observe people and build chip row
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
        if (people.isEmpty()) return

        // If no session person saved yet, or saved person no longer exists, pick first
        if (selectedPersonId == -1L || people.none { it.id == selectedPersonId }) {
            selectedPersonId = people.first().id
            viewModel.setPersonId(selectedPersonId)
            sessionManager.activePersonId = selectedPersonId
        }

        people.forEach { person ->
            val chip = Chip(requireContext()).apply {
                text = "${person.avatar} ${person.name}"
                isCheckable = true
                isChecked = person.id == selectedPersonId
                tag = person.id
                setOnClickListener {
                    selectedPersonId = person.id
                    viewModel.setPersonId(person.id)
                    sessionManager.activePersonId = person.id
                    for (i in 0 until binding.chipGroupPeople.childCount) {
                        val c = binding.chipGroupPeople.getChildAt(i) as? Chip
                        c?.isChecked = (c?.tag as? Long) == person.id
                    }
                }
            }
            binding.chipGroupPeople.addView(chip)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
