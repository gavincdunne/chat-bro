package com.weekendware.chatbro.util

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

abstract class MviViewModel<S : Any>(initialState: S) : ViewModel() {
    private val stateMutex = Mutex()
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    protected suspend fun updateState(transform: (S) -> S) {
        stateMutex.withLock {
            _state.value = transform(_state.value)
        }
    }
}