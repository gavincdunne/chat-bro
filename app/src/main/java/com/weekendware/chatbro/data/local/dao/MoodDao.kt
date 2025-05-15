package com.weekendware.chatbro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.weekendware.chatbro.data.local.entity.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert
    suspend fun insertMood(mood: MoodEntity)

    @Query("SELECT * FROM moods ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>
}