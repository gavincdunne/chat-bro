package com.weekendware.chatbro.di

import androidx.room.Room
import com.weekendware.chatbro.data.local.db.ChatBroDatabase
import com.weekendware.chatbro.data.repository.MoodRepository
import com.weekendware.chatbro.viewmodel.DashboardViewModel
import com.weekendware.chatbro.viewmodel.JournalViewModel
import com.weekendware.chatbro.viewmodel.MoodTrackerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // Provide Room database
    single {
        Room.databaseBuilder(
            androidContext(),
            ChatBroDatabase::class.java,
            "chatbro.db"
        ).build()
    }

    // Provide DAO
    single { get<ChatBroDatabase>().moodDao() }

    // Provide Repository
    single { MoodRepository(get()) }

    // Provide ViewModel
    viewModel { DashboardViewModel() }
    viewModel { MoodTrackerViewModel(get()) }
    viewModel { JournalViewModel() }
}
