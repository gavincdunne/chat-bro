
package com.weekendware.chatbro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weekendware.chatbro.domain.usecase.SaveCheckInUseCase
import kotlinx.coroutines.launch

class CheckInViewModel(private val saveCheckIn: SaveCheckInUseCase) : ViewModel() {
    fun saveMood(mood: String, journal: String) {
        viewModelScope.launch {
            saveCheckIn(mood, journal)
        }
    }
}
