package com.weekendware.chatbro.di

import com.weekendware.chatbro.viewmodel.DashboardViewModel
import com.weekendware.chatbro.viewmodel.JournalViewModel
import com.weekendware.chatbro.viewmodel.MoodTrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Provide ViewModel
    viewModel { DashboardViewModel() }
    viewModel { MoodTrackerViewModel() }
    viewModel { JournalViewModel() }
}
