package com.weekendware.chatbro.data.local

import androidx.room.TypeConverter
import com.weekendware.chatbro.domain.model.MoodType

class Converters {
    @TypeConverter
    fun fromTagList(tags: List<String>): String = tags.joinToString(",")

    @TypeConverter
    fun toTagList(data: String): List<String> =
        if (data.isBlank()) emptyList() else data.split(",")

    @TypeConverter
    fun fromMoodType(mood: MoodType?): String? = mood?.name

    @TypeConverter
    fun toMoodType(value: String?): MoodType? =
        value?.let { MoodType.valueOf(it) }
}