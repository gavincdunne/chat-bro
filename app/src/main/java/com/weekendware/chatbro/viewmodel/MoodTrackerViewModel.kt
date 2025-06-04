package com.weekendware.chatbro.viewmodel

import androidx.lifecycle.viewModelScope
import com.weekendware.chatbro.data.local.entity.MoodEntity
import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.remote.core.ApiResult
import com.weekendware.chatbro.data.remote.core.AppError
import com.weekendware.chatbro.data.remote.core.toUserMessage
import com.weekendware.chatbro.data.repository.MoodRepository
import com.weekendware.chatbro.domain.model.MoodType
import com.weekendware.chatbro.util.MviViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface MoodTrackerState {
    data object Idle : MoodTrackerState
    data object Loading : MoodTrackerState
    data class Success(val newEntry: MoodEntity) : MoodTrackerState
    data class Error(val exception: Throwable) : MoodTrackerState
    data class MoodSelected(val mood: MoodType, val note: String) : MoodTrackerState
}

sealed interface MoodIntent {
    data class SelectMood(val mood: MoodType) : MoodIntent
    data class UpdateNote(val note: String) : MoodIntent
    data object SaveMood : MoodIntent
    data object Reset : MoodIntent
}

class MoodTrackerViewModel(
    private val repository: MoodRepository,
    private val openAiService: OpenAiService
) : MviViewModel<MoodTrackerState>(MoodTrackerState.Idle) {

    val moodHistory: StateFlow<List<MoodEntity>> = repository.getAllMoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun processIntent(intent: MoodIntent) {
        when (intent) {
            is MoodIntent.SelectMood -> {
                viewModelScope.launch {
                    updateState { MoodTrackerState.MoodSelected(intent.mood, "") }
                }
            }
            is MoodIntent.UpdateNote -> {
                viewModelScope.launch {
                    updateState { current ->
                        if (current is MoodTrackerState.MoodSelected) {
                            current.copy(note = intent.note)
                        } else current
                    }
                }
            }
            MoodIntent.Reset -> {
                viewModelScope.launch {
                    updateState { MoodTrackerState.Idle }
                }
            }
            MoodIntent.SaveMood -> {
                val current = state.value as? MoodTrackerState.MoodSelected ?: return
                viewModelScope.launch {
                    updateState { MoodTrackerState.Loading }

                    val result = openAiService.getMoodInsight(current.mood.name, current.note)
                    val insight = when (result) {
                        is ApiResult.Success -> result.data
                        is ApiResult.Failure -> {
                            val error = result.throwable as? AppError ?: AppError.Unknown(result.throwable)
                            error.toUserMessage()
                        }
                    }

                    val moodEntry = MoodEntity(
                        mood = current.mood.name,
                        note = current.note,
                        insight = insight,
                        timestamp = System.currentTimeMillis()
                    )

                    try {
                        repository.insertMood(moodEntry)
                        updateState { MoodTrackerState.Success(moodEntry) }
                    } catch (e: Exception) {
                        updateState { MoodTrackerState.Error(e) }
                    }
                }
            }
        }
    }
}