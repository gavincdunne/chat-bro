package com.weekendware.chatbro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.weekendware.chatbro.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert
    suspend fun insertJournalEntry(entry: JournalEntity)

    @Query("SELECT * FROM journal ORDER BY timestamp DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntity>>
}