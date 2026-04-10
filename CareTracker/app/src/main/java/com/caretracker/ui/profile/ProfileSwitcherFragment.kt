package com.caretracker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.R
import com.caretracker.data.models.Person
import com.caretracker.data.repository.PersonRepository
import com.caretracker.databinding.FragmentProfileSwitcherBinding
import com.caretracker.utils.SessionManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileSwitcherFragment : BottomSheetDialogFragment() {

    @Inject lateinit var personRepository: PersonRepository
    @Inject lateinit var sessionManager: SessionManager

    private var _binding: FragmentProfileSwitcherBinding? = null
    private val binding get() = _binding!!

    var onProfileSelected: ((Person) -> Unit)? = null
    var onAddPersonClicked: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSwitcherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProfiles.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            val people = personRepository.getAllPeople().first()
            val activeId = sessionManager.activePersonId
            binding.rvProfiles.adapter = ProfileAdapter(people, activeId) { person ->
                lifecycleScope.launch {
                    personRepository.switchActivePerson(person.id)
                    sessionManager.activePersonId = person.id
                    onProfileSelected?.invoke(person)
                    dismiss()
                }
            }
        }

        binding.btnAddPerson.setOnClickListener {
            onAddPersonClicked?.invoke()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ── Inner RecyclerView Adapter ────────────────────────────────────────────

    private inner class ProfileAdapter(
        private val items: List<Person>,
        private val activeId: Long,
        private val onClick: (Person) -> Unit
    ) : RecyclerView.Adapter<ProfileAdapter.VH>() {

        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val avatar: TextView = view.findViewById(R.id.tvAvatar)
            val name: TextView = view.findViewById(R.id.tvName)
            val role: TextView = view.findViewById(R.id.tvRole)
            val check: TextView = view.findViewById(R.id.tvActiveCheck)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
            VH(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_person_profile, parent, false))

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: VH, position: Int) {
            val person = items[position]
            holder.avatar.text = person.avatar
            holder.name.text = person.name
            holder.role.text = person.role ?: ""
            holder.check.visibility = if (person.id == activeId) VISIBLE else GONE
            holder.itemView.setOnClickListener { onClick(person) }
        }
    }

    companion object {
        const val TAG = "ProfileSwitcherFragment"
    }
}
