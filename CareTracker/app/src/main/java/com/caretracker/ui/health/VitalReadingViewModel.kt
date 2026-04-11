package com.caretracker.ui.health

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caretracker.data.dao.VitalReadingDao
import com.caretracker.data.models.VitalReading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VitalReadingViewModel @Inject constructor(
    private val dao: VitalReadingDao
) : ViewModel() {

    fun getAll(personId: Long): Flow<List<VitalReading>> = dao.getAllForPerson(personId)

    fun getByType(personId: Long, type: String): Flow<List<VitalReading>> =
        dao.getByType(personId, type)

    fun getByTypeInRange(personId: Long, type: String, from: String, to: String): Flow<List<VitalReading>> =
        dao.getByTypeInRange(personId, type, from, to)

    suspend fun getById(id: Long): VitalReading? = dao.getById(id)

    fun save(reading: VitalReading) = viewModelScope.launch {
        if (reading.id == 0L) dao.insert(reading) else dao.update(reading)
    }

    fun delete(reading: VitalReading) = viewModelScope.launch { dao.delete(reading) }
    fun deleteById(id: Long) = viewModelScope.launch { dao.deleteById(id) }
}
