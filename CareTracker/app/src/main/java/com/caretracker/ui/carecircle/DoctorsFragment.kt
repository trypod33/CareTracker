package com.caretracker.ui.carecircle

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var doctorJob: Job? = null

    /** -1L = "All" chip; any other value = filter by that person */
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

        // Default to active person if one is selected, otherwise show All
        val activePerson = sessionManager.activePersonId
        selectedPersonId = if (activePerson != -1L) activePerson else -1L

        // Observe people for chips ONLY — does NOT call loadDoctors() to avoid
        // cancelling the active doctor-list job via collectLatest.
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPeople.collect { people ->
                buildMemberChips(people)
            }
        }

        // Initial doctor list load
        loadDoctors(selectedPersonId)

        binding.fabAddDoctor.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditDoctorActivity::class.java))
        }
    }

    // ── Member filter chip row ─────────────────────────────────────────────

    private fun buildMemberChips(people: List<Person>) {
        val container = binding.layoutMemberChips
        container.removeAllViews()
        container.addView(makeChip("All", -1L))
        people.forEach { person ->
            container.addView(makeChip("${person.avatar} ${person.name}", person.id))
        }
    }

    private fun makeChip(label: String, personId: Long): TextView {
        val density = resources.displayMetrics.density
        val dp8  = (8  * density).toInt()
        val dp16 = (16 * density).toInt()
        val dp36 = (36 * density).toInt()
        return TextView(requireContext()).apply {
            text = label
            textSize = 13f
            minHeight = dp36
            gravity = android.view.Gravity.CENTER
            setPadding(dp16, dp8, dp16, dp8)
            background = ContextCompat.getDrawable(
                requireContext(),
                if (selectedPersonId == personId) R.drawable.bg_chip_selected
                else R.drawable.bg_chip_unselected
            )
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).also { it.marginEnd = dp8 }
            setOnClickListener {
                selectedPersonId = personId
                // Redraw all chips with updated selection
                buildMemberChips(viewModel.allPeople.value)
                loadDoctors(personId)
            }
        }
    }

    // ── Doctor list loader ────────────────────────────────────────────────

    private fun loadDoctors(personId: Long) {
        doctorJob?.cancel()
        doctorJob = viewLifecycleOwner.lifecycleScope.launch {
            val flow = if (personId == -1L) viewModel.allDoctors
                       else viewModel.getDoctorsForPerson(personId)
            flow.collectLatest { doctors ->
                adapter.submitList(doctors)
                binding.tvEmptyDoctors.visibility =
                    if (doctors.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        doctorJob?.cancel()
        _binding = null
    }
}
