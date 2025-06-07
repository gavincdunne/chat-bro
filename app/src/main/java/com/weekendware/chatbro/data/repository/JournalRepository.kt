package com.weekendware.chatbro.data.repository

import com.weekendware.chatbro.data.local.dao.JournalDao
import com.weekendware.chatbro.data.local.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val dao: JournalDao) {
    suspend fun insertJournalEntry(entry: JournalEntity) = dao.insertJournalEntry(entry)
    fun getAllJournalEntries(): Flow<List<JournalEntity>> = dao.getAllJournalEntries()
}
