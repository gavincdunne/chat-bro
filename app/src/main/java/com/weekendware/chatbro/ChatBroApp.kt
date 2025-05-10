package com.weekendware.chatbro

import android.app.Application
import com.weekendware.chatbro.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChatBroApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ChatBroApp)
            modules(appModule)
        }
    }
}
