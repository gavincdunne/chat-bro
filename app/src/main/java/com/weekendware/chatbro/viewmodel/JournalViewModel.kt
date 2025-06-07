package com.weekendware.chatbro.viewmodel

import androidx.lifecycle.viewModelScope
import com.weekendware.chatbro.data.local.entity.JournalEntity
import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.repository.JournalRepository
import com.weekendware.chatbro.domain.model.MoodType
import com.weekendware.chatbro.util.MviViewModel
import kotlinx.coroutines.launch


sealed interface JournalState {
    data object Idle : JournalState
    data object Loading : JournalState
    data class Editing(
        val title: String = "",
        val body: String = "",
        val tags: List<String> = emptyList(),
        val mood: MoodType? = null,
        val isSaveEnabled: Boolean = false
    ) : JournalState

    data class Success(val savedEntry: JournalEntity) : JournalState
    data class Error(val exception: Throwable) : JournalState
}

sealed interface JournalIntent {
    data class UpdateTitle(val title: String) : JournalIntent
    data class UpdateBody(val body: String) : JournalIntent
    data class AddTag(val tag: String) : JournalIntent
    data class RemoveTag(val tag: String) : JournalIntent
    data class SetMood(val mood: MoodType) : JournalIntent
    data object SaveEntry : JournalIntent
    data object Reset : JournalIntent
}

class JournalViewModel(
    private val repository: JournalRepository,
    private val openAiService: OpenAiService
) : MviViewModel<JournalState>(JournalState.Idle) {

    fun processIntent(intent: JournalIntent) {
        when (intent) {
            is JournalIntent.UpdateTitle ->
                viewModelScope.launch {
                    updateState {
                        val prev = it.asEditingOrDefault()
                        prev.copy(title = intent.title, isSaveEnabled = intent.title.isNotBlank() || prev.body.isNotBlank())
                    }
                }

            is JournalIntent.UpdateBody ->
                viewModelScope.launch {
                    updateState {
                        val prev = it.asEditingOrDefault()
                        prev.copy(body = intent.body, isSaveEnabled = intent.body.isNotBlank())
                    }
                }

            is JournalIntent.AddTag ->
                viewModelScope.launch {
                    updateState {
                        val prev = it.asEditingOrDefault()
                        prev.copy(tags = prev.tags + intent.tag)
                    }
                }

            is JournalIntent.RemoveTag ->
                viewModelScope.launch {
                    updateState {
                        val prev = it.asEditingOrDefault()
                        prev.copy(tags = prev.tags - intent.tag)
                    }
                }

            is JournalIntent.SetMood ->
                viewModelScope.launch {
                    updateState {
                        val prev = it.asEditingOrDefault()
                        prev.copy(mood = intent.mood)
                    }
                }

            JournalIntent.Reset ->
                viewModelScope.launch {
                    updateState { JournalState.Idle }
                }

            JournalIntent.SaveEntry -> {
                val current = state.value.asEditingOrNull() ?: return
                if (current.body.isBlank()) return

                viewModelScope.launch {
                    updateState { JournalState.Loading }
                    try {
                        val entry = JournalEntity(
                            title = current.title,
                            body = current.body,
                            tags = current.tags,
                            mood = current.mood,
                            timestamp = System.currentTimeMillis()
                        )
                        repository.insertJournalEntry(entry)
                        updateState { JournalState.Success(entry) }
                    } catch (e: Exception) {
                        updateState { JournalState.Error(e) }
                    }
                }
            }
        }
    }

    private fun JournalState.asEditingOrDefault(): JournalState.Editing {
        return this as? JournalState.Editing ?: JournalState.Editing()
    }

    private fun JournalState.asEditingOrNull(): JournalState.Editing? {
        return this as? JournalState.Editing
    }
}
