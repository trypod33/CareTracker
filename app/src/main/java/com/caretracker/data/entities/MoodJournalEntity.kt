package com.caretracker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_journal")
data class MoodJournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val entryDate: String,
    val moodScore: Int? = null,
    val content: String? = null,
    val tags: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
