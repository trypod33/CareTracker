package com.caretracker.data.database

import androidx.room.*
import com.caretracker.data.models.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM people ORDER BY name ASC")
    fun getAllPeople(): Flow<List<Person>>

    @Query("SELECT * FROM people WHERE id = :id")
    suspend fun getPersonById(id: Long): Person?

    @Query("SELECT * FROM people WHERE isActiveProfile = 1 LIMIT 1")
    suspend fun getActivePerson(): Person?

    @Query("SELECT * FROM people WHERE isActiveProfile = 1 LIMIT 1")
    fun observeActivePerson(): Flow<Person?>

    /** Clear active flag on all persons — call before setting a new one. */
    @Query("UPDATE people SET isActiveProfile = 0")
    suspend fun clearActiveProfile()

    /** Set a specific person as the active profile. */
    @Query("UPDATE people SET isActiveProfile = 1 WHERE id = :personId")
    suspend fun setActiveProfile(personId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: Person): Long

    @Update
    suspend fun update(person: Person)

    @Delete
    suspend fun delete(person: Person)
}
