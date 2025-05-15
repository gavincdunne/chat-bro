package com.weekendware.chatbro.di

import androidx.room.Room
import com.weekendware.chatbro.data.local.db.ChatBroDatabase
import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.repository.MoodRepository
import com.weekendware.chatbro.viewmodel.DashboardViewModel
import com.weekendware.chatbro.viewmodel.JournalViewModel
import com.weekendware.chatbro.viewmodel.MoodTrackerViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.weekendware.chatbro.BuildConfig


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

    // 4. OpenAI Service
    single {
        val apiKey = BuildConfig.OPENAI_API_KEY
        OpenAiService(apiKey)
    }

    // Provide ViewModel
    viewModel { DashboardViewModel() }
    viewModel { MoodTrackerViewModel(get(), get()) }
    viewModel { JournalViewModel() }
}
