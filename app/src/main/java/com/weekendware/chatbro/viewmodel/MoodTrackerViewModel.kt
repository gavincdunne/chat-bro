package com.weekendware.chatbro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weekendware.chatbro.data.local.entity.MoodEntity
import com.weekendware.chatbro.data.repository.MoodRepository
import com.weekendware.chatbro.domain.model.MoodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch


class MoodTrackerViewModel(private val repository: MoodRepository): ViewModel() {

    private val _currentMood = MutableStateFlow<MoodType?>(null)
    val currentMood: StateFlow<MoodType?> = _currentMood

    val moodHistory: StateFlow<List<MoodEntity>> = repository.getAllMoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectMood(mood: MoodType) {
        _currentMood.value = mood
    }

    fun addMoodEntry(note: String? = null) {
        _currentMood.value?.let { mood ->
            viewModelScope.launch {
                repository.insertMood(
                    MoodEntity(mood = mood.name, timestamp = System.currentTimeMillis())
                )

                _currentMood.value = null
            }
        }
    }
}