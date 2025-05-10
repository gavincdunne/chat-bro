package com.weekendware.chatbro.viewmodel

import androidx.lifecycle.ViewModel
import com.weekendware.chatbro.domain.model.MoodEntry
import com.weekendware.chatbro.domain.model.MoodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MoodTrackerViewModel: ViewModel() {

    private val _currentMood = MutableStateFlow<MoodType?>(null)
    val currentMood: StateFlow<MoodType?> = _currentMood

    private val _moodEntries = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodEntries: StateFlow<List<MoodEntry>> = _moodEntries

    fun selectMood(mood: MoodType) {
        _currentMood.value = mood
    }

    fun addMoodEntry(note: String? = null) {
        _currentMood.value?.let {
            val entry = MoodEntry(it, note = note)
            _moodEntries.value = moodEntries.value + entry
            _currentMood.value = null
        }
    }
}