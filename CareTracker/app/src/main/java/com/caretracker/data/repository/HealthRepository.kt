package com.caretracker.data.repository

import com.caretracker.data.dao.DailyHealthEntryDao
import com.caretracker.data.models.DailyHealthEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepository @Inject constructor(
    private val dao: DailyHealthEntryDao
) {
    fun getAllForPerson(personId: Long): Flow<List<DailyHealthEntry>> =
        dao.getAllForPerson(personId)

    suspend fun getEntryForDate(personId: Long, date: String): DailyHealthEntry? =
        dao.getEntryForDate(personId, date)

    suspend fun getById(id: Long): DailyHealthEntry? = dao.getById(id)

    suspend fun save(entry: DailyHealthEntry) {
        if (entry.id == 0L) dao.insert(entry) else dao.update(entry)
    }

    suspend fun delete(entry: DailyHealthEntry) = dao.delete(entry)
}
