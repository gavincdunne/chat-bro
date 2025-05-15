package com.weekendware.chatbro.data.repository

import com.weekendware.chatbro.data.local.dao.MoodDao
import com.weekendware.chatbro.data.local.entity.MoodEntity
import kotlinx.coroutines.flow.Flow

class MoodRepository(private val dao: MoodDao) {
    suspend fun insertMood(mood: MoodEntity) = dao.insertMood(mood)
    fun getAllMoods(): Flow<List<MoodEntity>> = dao.getAllMoods()
}
