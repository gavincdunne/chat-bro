package com.weekendware.chatbro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.weekendware.chatbro.domain.model.MoodType

@Entity(tableName = "journal")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val body: String,
    val tags: List<String>,
    val mood: MoodType?,
    val timestamp: Long
)