package com.caretracker.data.repository

import com.caretracker.data.database.PersonDao
import com.caretracker.data.models.Person
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(
    private val personDao: PersonDao
) {
    val allPeople: Flow<List<Person>> = personDao.getAllPeople()

    suspend fun getPersonById(id: Long): Person? {
        return personDao.getPersonById(id)
    }

    suspend fun insertPerson(person: Person): Long {
        return personDao.insert(person)
    }

    suspend fun updatePerson(person: Person) {
        personDao.update(person)
    }

    suspend fun deletePerson(person: Person) {
        personDao.delete(person)
    }
}