package com.weekendware.chatbro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weekendware.chatbro.ui.DashboardScreen
import com.weekendware.chatbro.ui.MoodTrackerScreen
import com.weekendware.chatbro.ui.JournalScreen


sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Mood : Screen("mood")
    object Journal : Screen("journal")
}

@Composable
fun ChatBroNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
        composable(Screen.Dashboard.route) { DashboardScreen() }
        composable(Screen.Mood.route) { MoodTrackerScreen() }
        composable(Screen.Journal.route) { JournalScreen() }
    }
}