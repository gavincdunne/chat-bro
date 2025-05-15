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
        val moodType = currentMood.value ?: return

        viewModelScope.launch {
            val insight = try {
                openAiService.getMoodInsight(moodType.name, note)
            } catch (e: Exception) {
                e.printStackTrace()
                "AI insight unavailable (rate limit hit)."
            }

            val moodEntity = MoodEntity(
                mood = moodType.name,
                note = note,
                insight = insight,
                timestamp = System.currentTimeMillis()
            )

            repository.insertMood(moodEntity)

            _currentMood.value = null
        }
    }

}