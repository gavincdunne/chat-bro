package com.weekendware.chatbro.data.local.entity

data class JournalEntryEntity(
    val id: Int = 0,
    val text: String,
    val timestamp: Long,
    val insight: String? = null
)
