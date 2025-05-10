package com.weekendware.chatbro.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.weekendware.chatbro.data.local.dao.CheckInDao
import com.weekendware.chatbro.data.local.entity.CheckInEntity

@Database(entities = [CheckInEntity::class], version = 1, exportSchema = false)
abstract class ChatBroDatabase : RoomDatabase() {
    abstract fun checkInDao(): CheckInDao

    companion object {
        fun getInstance(context: Context): ChatBroDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ChatBroDatabase::class.java,
                "chatbro.db"
            ).build()
        }
    }
}
