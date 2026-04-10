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
import com.caretracker.databinding.FragmentMembersBinding
import com.caretracker.ui.people.AddEditPersonActivity
import com.caretracker.ui.people.PeopleAdapter
import com.caretracker.ui.people.PersonViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MembersFragment : Fragment() {

    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PersonViewModel by viewModels()
    private lateinit var adapter: PeopleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PeopleAdapter(
            onClick = { person ->
                startActivity(
                    Intent(requireContext(), AddEditPersonActivity::class.java)
                        .putExtra("PERSON_ID", person.id)
                )
            },
            onDelete = { person -> viewModel.deletePerson(person) }
        )

        binding.rvMembers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMembers.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPeople.collect { people ->
                adapter.submitList(people)
                binding.tvEmptyMembers.visibility =
                    if (people.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.fabAddMember.setOnClickListener {
            startActivity(Intent(requireContext(), AddEditPersonActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
