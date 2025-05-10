package com.weekendware.chatbro.di

import com.weekendware.chatbro.data.repository.CheckInRepository
import com.weekendware.chatbro.domain.usecase.SaveCheckInUseCase
import com.weekendware.chatbro.viewmodel.CheckInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provide Repository (replace with real impl if needed)
    single { CheckInRepository() }

    // Provide UseCase
    single { SaveCheckInUseCase(get()) }

    // Provide ViewModel
    viewModel { CheckInViewModel(get()) }
}
