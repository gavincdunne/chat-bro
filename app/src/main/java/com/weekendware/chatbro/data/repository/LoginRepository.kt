package com.weekendware.chatbro.data.repository

import com.weekendware.chatbro.data.local.dao.UserDao
import com.weekendware.chatbro.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class LoginRepository(private val dao: UserDao) {
    suspend fun insertUser(user: UserEntity) = dao.insertUser(user)
    fun getCurrentUser(): Flow<UserEntity> = dao.getCurrentUser()
}