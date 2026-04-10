package com.caretracker.data.repository

import com.caretracker.data.database.HealthLogDao
import com.caretracker.data.models.HealthLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepository @Inject constructor(
    private val healthLogDao: HealthLogDao
) {
    fun getAllLogsForPerson(personId: Long): Flow<List<HealthLog>> =
        healthLogDao.getAllLogsForPerson(personId)

    fun getLogsForPersonByType(personId: Long, metricId: Long): Flow<List<HealthLog>> =
        healthLogDao.getLogsForPersonByType(personId, metricId)

    suspend fun insertLog(log: HealthLog): Long =
        healthLogDao.insert(log)

    suspend fun updateLog(log: HealthLog) =
        healthLogDao.update(log)

    suspend fun deleteLog(log: HealthLog) =
        healthLogDao.delete(log)
}
