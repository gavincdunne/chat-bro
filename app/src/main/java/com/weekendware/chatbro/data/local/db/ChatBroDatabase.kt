package com.weekendware.chatbro.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.weekendware.chatbro.data.local.dao.MoodDao
import com.weekendware.chatbro.data.local.entity.MoodEntity

@Database(entities = [MoodEntity::class], version = 1, exportSchema = false)
abstract class ChatBroDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao

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
