package com.caretracker.ui.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.models.Person
import com.caretracker.data.repository.PersonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    private val repository: PersonRepository
) : ViewModel() {

    // repository.getAllPeople() matches PersonRepository fun getAllPeople()
    val allPeople: StateFlow<List<Person>> = repository.getAllPeople()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun savePerson(person: Person) {
        viewModelScope.launch {
            if (person.id == 0L) {
                repository.insert(person)
            } else {
                repository.update(person)
            }
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {
            repository.delete(person)
        }
    }

    fun switchActivePerson(personId: Long) {
        viewModelScope.launch {
            repository.switchActivePerson(personId)
        }
    }
}
