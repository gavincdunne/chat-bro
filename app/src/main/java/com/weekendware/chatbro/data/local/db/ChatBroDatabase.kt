package com.weekendware.chatbro.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.weekendware.chatbro.data.local.Converters
import com.weekendware.chatbro.data.local.dao.JournalDao
import com.weekendware.chatbro.data.local.dao.MoodDao
import com.weekendware.chatbro.data.local.dao.UserDao
import com.weekendware.chatbro.data.local.entity.JournalEntity
import com.weekendware.chatbro.data.local.entity.MoodEntity
import com.weekendware.chatbro.data.local.entity.UserEntity

@TypeConverters(Converters::class)
@Database(entities = [UserEntity::class,MoodEntity::class, JournalEntity::class], version = 1, exportSchema = false)
abstract class ChatBroDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun moodDao(): MoodDao
    abstract fun journalDao(): JournalDao

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
