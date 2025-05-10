package com.weekendware.chatbro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.weekendware.chatbro.ui.navigation.ChatBroNavGraph
import com.weekendware.chatbro.ui.theme.ChatBroTheme
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ChatBroTheme {
                val navController = rememberNavController()
                ChatBroNavGraph(navController)
            }
        }
    }
}