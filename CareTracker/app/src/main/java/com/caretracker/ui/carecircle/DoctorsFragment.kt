package com.caretracker.ui.carecircle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.R
import com.caretracker.data.models.Person
import com.caretracker.databinding.FragmentDoctorsBinding
import com.caretracker.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DoctorsFragment : Fragment() {

    private var _binding: FragmentDoctorsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorViewModel by viewModels()
    private lateinit var adapter: DoctorAdapter
    private var collectJob: Job? = null

    // -1L means "All" chip selected
    private var selectedPersonId: Long = -1L

    @Inject lateinit var sessionManager: SessionManager

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

        // Pre-select active person (or All if none)
        selectedPersonId = sessionManager.activePersonId

        // Build member filter chips once people are loaded
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPeople.collectLatest { people ->
                buildMemberChips(people)
                // Reload doctor list whenever people changes (covers the case where
                // someone is added/removed after this screen is already open)
                loadDoctors(selectedPersonId)
            }
        }

        binding.fabAddDoctor.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditDoctorActivity::class.java))
        }
    }

    // ── Member filter chip row ─────────────────────────────────────────────

    private fun buildMemberChips(people: List<Person>) {
        val chipContainer = binding.layoutMemberChips
        chipContainer.removeAllViews()

        // "All" chip
        chipContainer.addView(makeChip("All", -1L, selectedPersonId == -1L))

        // One chip per member
        people.forEach { person ->
            chipContainer.addView(
                makeChip("${person.avatar} ${person.name}", person.id,
                    selectedPersonId == person.id)
            )
        }
    }

    private fun makeChip(label: String, personId: Long, selected: Boolean): TextView {
        val dp8 = (8 * resources.displayMetrics.density).toInt()
        val dp16 = (16 * resources.displayMetrics.density).toInt()
        return TextView(requireContext()).apply {
            text = label
            textSize = 13f
            setPadding(dp16, dp8, dp16, dp8)
            isSelected = selected
            background = ContextCompat.getDrawable(
                requireContext(),
                if (selected) R.drawable.bg_chip_selected else R.drawable.bg_chip_unselected
            )
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.marginEnd = dp8
            layoutParams = lp
            setOnClickListener {
                selectedPersonId = personId
                // Rebuild chips to update selected state
                buildMemberChips(viewModel.allPeople.value)
                loadDoctors(personId)
            }
        }
    }

    // ── Doctor list loader ─────────────────────────────────────────────────

    private fun loadDoctors(personId: Long) {
        collectJob?.cancel()
        collectJob = viewLifecycleOwner.lifecycleScope.launch {
            val flow = if (personId == -1L) {
                viewModel.allDoctors
            } else {
                viewModel.getDoctorsForPerson(personId)
            }
            flow.collectLatest { doctors ->
                adapter.submitList(doctors)
                binding.tvEmptyDoctors.visibility =
                    if (doctors.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        collectJob?.cancel()
        _binding = null
    }
}
