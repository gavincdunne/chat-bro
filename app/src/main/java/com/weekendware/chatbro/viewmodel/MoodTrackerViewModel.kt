package com.weekendware.chatbro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weekendware.chatbro.data.local.entity.MoodEntity
import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.repository.MoodRepository
import com.weekendware.chatbro.domain.model.MoodType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch


class MoodTrackerViewModel(
    private val repository: MoodRepository,
    private val openAiService: OpenAiService
): ViewModel() {

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
                val moodName = mood.name

                val insight = try {
                    openAiService.getMoodInsight(moodName, note)
                } catch (e: Exception) {
                    e.printStackTrace()
                    "AI insight unavailable (rate limit hit)."
                }

                val entry = MoodEntity(
                    mood = moodName,
                    timestamp = System.currentTimeMillis(),
                    insight = insight
                )
                repository.insertMood(entry)

                println("AI Insight: $insight")

                _currentMood.value = null
            }
        }
    }
}