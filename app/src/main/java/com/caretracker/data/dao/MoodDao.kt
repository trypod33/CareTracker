package com.caretracker.data.dao

import androidx.room.*
import com.caretracker.data.entities.MoodJournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_journal WHERE userId = :userId ORDER BY entryDate DESC")
    fun getMoodEntriesForUser(userId: Long): Flow<List<MoodJournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: MoodJournalEntity): Long

    @Update
    suspend fun updateEntry(entry: MoodJournalEntity)

    @Delete
    suspend fun deleteEntry(entry: MoodJournalEntity)
}
