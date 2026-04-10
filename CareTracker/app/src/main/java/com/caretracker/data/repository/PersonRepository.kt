package com.caretracker.data.repository

import com.caretracker.data.database.PersonDao
import com.caretracker.data.models.Person
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonRepository @Inject constructor(private val personDao: PersonDao) {

    fun getAllPeople(): Flow<List<Person>> = personDao.getAllPeople()

    suspend fun getPersonById(id: Long): Person? = personDao.getPersonById(id)

    fun observeActivePerson(): Flow<Person?> = personDao.observeActivePerson()

    suspend fun getActivePerson(): Person? = personDao.getActivePerson()

    /**
     * Switch the active profile to [personId].
     * Clears any previously active flag first so only one person is ever active.
     */
    suspend fun switchActivePerson(personId: Long) {
        personDao.clearActiveProfile()
        personDao.setActiveProfile(personId)
    }

    suspend fun insert(person: Person): Long = personDao.insert(person)

    suspend fun update(person: Person) = personDao.update(person)

    suspend fun delete(person: Person) = personDao.delete(person)
}
