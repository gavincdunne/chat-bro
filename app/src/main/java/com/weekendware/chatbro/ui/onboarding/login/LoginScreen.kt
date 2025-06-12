package com.weekendware.chatbro.ui.onboarding.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.weekendware.chatbro.viewmodel.onboarding.LoginState
import com.weekendware.chatbro.viewmodel.onboarding.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(state) {
        when(state) {
            is LoginState.Success -> {

            }
            is LoginState.Error -> {

            }
        }
    }
}