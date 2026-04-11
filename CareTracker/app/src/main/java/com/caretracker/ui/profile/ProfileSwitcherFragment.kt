package com.caretracker.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
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

/**
 * Bottom sheet that lets the user switch between person profiles.
 *
 * Results are communicated back to the host via the Fragment Result API
 * (setFragmentResult) rather than public lambda properties, which avoids
 * memory leaks when the fragment is recreated by the system.
 *
 * Listen in the host (e.g. MainActivity) with:
 *
 *   supportFragmentManager.setFragmentResultListener(
 *       ProfileSwitcherFragment.REQUEST_KEY, this
 *   ) { _, bundle ->
 *       when (bundle.getString(ProfileSwitcherFragment.KEY_ACTION)) {
 *           ProfileSwitcherFragment.ACTION_PROFILE_SELECTED -> {
 *               val name   = bundle.getString(ProfileSwitcherFragment.KEY_NAME)
 *               val avatar = bundle.getString(ProfileSwitcherFragment.KEY_AVATAR)
 *               // update UI
 *           }
 *           ProfileSwitcherFragment.ACTION_ADD_PERSON -> {
 *               // navigate to Care Circle
 *           }
 *       }
 *   }
 */
@AndroidEntryPoint
class ProfileSwitcherFragment : BottomSheetDialogFragment() {

    @Inject lateinit var personRepository: PersonRepository
    @Inject lateinit var sessionManager: SessionManager

    private var _binding: FragmentProfileSwitcherBinding? = null
    private val binding get() = _binding!!

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
                    setFragmentResult(
                        REQUEST_KEY,
                        bundleOf(
                            KEY_ACTION to ACTION_PROFILE_SELECTED,
                            KEY_PERSON_ID to person.id,
                            KEY_NAME to person.name,
                            KEY_AVATAR to person.avatar
                        )
                    )
                    dismiss()
                }
            }
        }

        binding.btnAddPerson.setOnClickListener {
            setFragmentResult(
                REQUEST_KEY,
                bundleOf(KEY_ACTION to ACTION_ADD_PERSON)
            )
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

        // Fragment Result API keys
        const val REQUEST_KEY          = "profile_switcher_result"
        const val KEY_ACTION           = "action"
        const val KEY_PERSON_ID        = "person_id"
        const val KEY_NAME             = "name"
        const val KEY_AVATAR           = "avatar"
        const val ACTION_PROFILE_SELECTED = "profile_selected"
        const val ACTION_ADD_PERSON    = "add_person"
    }
}
