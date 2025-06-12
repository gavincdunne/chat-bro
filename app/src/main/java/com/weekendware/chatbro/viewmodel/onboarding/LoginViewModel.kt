package com.weekendware.chatbro.viewmodel.onboarding

import com.weekendware.chatbro.data.local.entity.UserEntity
import com.weekendware.chatbro.util.MviViewModel

sealed interface LoginState {
    data object Idle: LoginState
    data class Success(val user: UserEntity): LoginState
    data class Error(val exception: Throwable): LoginState
}

sealed interface LoginIntent {
    data object CreateNewUser: LoginIntent
    data object Login: LoginIntent

}

class LoginViewModel: MviViewModel<LoginState>(LoginState.Idle) {
}