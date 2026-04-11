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
import com.caretracker.utils.SessionManager
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MedicationAdapter(
            onClick  = { med ->
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

        // ── React to top-right profile chip changes via SessionManager StateFlow ──
        // This means the list updates instantly when the user switches profiles,
        // with NO need for a fragment detach/reattach.
        viewLifecycleOwner.lifecycleScope.launch {
            sessionManager.activePersonIdFlow.collectLatest { personId ->
                if (personId == -1L) {
                    // No person selected yet — show placeholder, hide list
                    binding.tvBannerName.text = "Select a person using the button in the top right"
                    binding.tvBannerAvatar.text = "\uD83D\uDC64"
                    binding.tvEmptyMeds.visibility = View.VISIBLE
                    binding.tvEmptyMeds.text = "Select a person using the ▶ button in the top right"
                    adapter.submitList(emptyList())
                } else {
                    // Tell the ViewModel to filter by this person
                    viewModel.setPersonId(personId)
                }
            }
        }

        // ── Update banner name from the people list ─────────────────────────────
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPeople.collectLatest { people ->
                val activePerson = people.firstOrNull { it.id == sessionManager.activePersonId }
                if (activePerson != null) {
                    binding.tvBannerAvatar.text = activePerson.avatar
                    binding.tvBannerName.text   = activePerson.name
                }
            }
        }

        // ── Observe filtered medication list ──────────────────────────────────
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.medications.collectLatest { meds ->
                adapter.submitList(meds)
                binding.tvEmptyMeds.visibility =
                    if (meds.isEmpty() && sessionManager.activePersonId != -1L)
                        View.VISIBLE else View.GONE
            }
        }

        // FAB: open AddEditMed for the currently selected person
        binding.fabAddMed.setOnClickListener {
            val personId = sessionManager.activePersonId
            if (personId == -1L) return@setOnClickListener   // no person selected
            startActivity(
                Intent(requireContext(), AddEditMedActivity::class.java)
                    .putExtra("PERSON_ID", personId)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
