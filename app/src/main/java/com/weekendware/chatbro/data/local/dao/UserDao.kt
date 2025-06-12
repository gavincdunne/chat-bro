package com.weekendware.chatbro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.weekendware.chatbro.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(mood: UserEntity)

    @Query("SELECT * FROM user")
    fun getCurrentUser(): Flow<UserEntity>
}