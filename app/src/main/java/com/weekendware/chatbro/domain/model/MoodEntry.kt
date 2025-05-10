package com.weekendware.chatbro.domain.model

data class MoodEntry(
    val mood: MoodType,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String? = null
)
