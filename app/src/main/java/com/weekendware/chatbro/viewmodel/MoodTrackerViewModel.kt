package com.weekendware.chatbro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weekendware.chatbro.data.local.entity.MoodEntity
import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.remote.core.ApiResult
import com.weekendware.chatbro.data.remote.core.AppError
import com.weekendware.chatbro.data.remote.core.toUserMessage
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

    sealed interface MoodTrackerState {
        data object Idle : MoodTrackerState
        data object Loading : MoodTrackerState
        data class Success(val newEntry: MoodEntity) : MoodTrackerState
        data class Error(val exception: Throwable) : MoodTrackerState
        data class MoodSelected(val mood: MoodType, val note: String) : MoodTrackerState
    }

    private val _state = MutableStateFlow<MoodTrackerState>(MoodTrackerState.Idle)
    val state: StateFlow<MoodTrackerState> = _state

    val moodHistory: StateFlow<List<MoodEntity>> = repository.getAllMoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectMood(mood: MoodType, note: String = "") {
        _state.value = MoodTrackerState.MoodSelected(mood, note)
    }

    fun updateNote(note: String) {
        val current = _state.value
        if (current is MoodTrackerState.MoodSelected) {
            _state.value = current.copy(note = note)
        }
    }

    fun resetState() {
        _state.value = MoodTrackerState.Idle
    }

    fun saveMood() {
        val current = _state.value as? MoodTrackerState.MoodSelected ?: return

        viewModelScope.launch {
            val result = openAiService.getMoodInsight(current.mood.name, current.note)

            val insight = when (result) {
                is ApiResult.Success -> { result.data }
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
                _state.value = MoodTrackerState.Success(moodEntry)
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = MoodTrackerState.Error(e)
            }
        }
    }
}