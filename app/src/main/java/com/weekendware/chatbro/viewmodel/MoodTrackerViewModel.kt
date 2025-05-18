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
import com.weekendware.chatbro.data.common.Result



class MoodTrackerViewModel(
    private val repository: MoodRepository,
    private val openAiService: OpenAiService
): ViewModel() {

    private val _currentMood = MutableStateFlow<MoodType?>(null)
    val currentMood: StateFlow<MoodType?> = _currentMood

    val moodHistory: StateFlow<List<MoodEntity>> = repository.getAllMoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _aiInsightState = MutableStateFlow<Result<String>?>(null)
    val aiInsightState: StateFlow<Result<String>?> = _aiInsightState

    fun selectMood(mood: MoodType) {
        _currentMood.value = mood
    }

    fun addMoodEntry(note: String? = null) {
        val moodType = currentMood.value ?: return

        viewModelScope.launch {
            _aiInsightState.value = Result.Loading

            val insightResult = openAiService.getMoodInsight(moodType.name, note)

            val insight = when (insightResult) {
                is Result.Success -> {
                    _aiInsightState.value = insightResult
                    insightResult.data
                }
                is Result.Error -> {
                    val message = "AI insight unavailable: ${insightResult.message}"
                    insightResult.throwable?.printStackTrace()
                    _aiInsightState.value = Result.Error(message, insightResult.throwable)
                    message
                }
                is Result.Loading -> {
                    _aiInsightState.value = Result.Loading
                    "Still loading..."
                }
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