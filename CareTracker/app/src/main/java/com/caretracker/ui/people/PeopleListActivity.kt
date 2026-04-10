package com.caretracker.ui.people

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.caretracker.databinding.ActivityPeopleListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PeopleListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPeopleListBinding
    private val viewModel: PersonViewModel by viewModels()
    private lateinit var adapter: PeopleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPeopleListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Care Circle"

        adapter = PeopleAdapter(
            onClick = { person ->
                startActivity(
                    Intent(this, AddEditPersonActivity::class.java)
                        .putExtra("PERSON_ID", person.id)
                )
            },
            onDelete = { person -> viewModel.deletePerson(person) }
        )

        binding.rvPeople.adapter = adapter

        binding.fabAddPerson.setOnClickListener {
            startActivity(Intent(this, AddEditPersonActivity::class.java))
        }

        observePeople()
    }

    private fun observePeople() {
        lifecycleScope.launch {
            viewModel.allPeople.collect { people ->
                adapter.submitList(people)
            }
        }
    }
}
