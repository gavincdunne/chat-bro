package com.weekendware.chatbro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moods")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mood: String,
    val timestamp: Long = System.currentTimeMillis()
)