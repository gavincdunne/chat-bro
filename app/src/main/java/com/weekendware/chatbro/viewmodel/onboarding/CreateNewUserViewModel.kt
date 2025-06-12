package com.weekendware.chatbro.viewmodel.onboarding

import com.weekendware.chatbro.data.local.entity.UserEntity
import com.weekendware.chatbro.util.MviViewModel

sealed interface CreateNewUserState {
    data object Idle: CreateNewUserState
    data class Success(val user: UserEntity): CreateNewUserState
    data class Error(val exception: Throwable): CreateNewUserState
}

sealed interface CreateNewUserIntent {

}

class CreateNewUserViewModel: MviViewModel<CreateNewUserState>(CreateNewUserState.Idle) {

}