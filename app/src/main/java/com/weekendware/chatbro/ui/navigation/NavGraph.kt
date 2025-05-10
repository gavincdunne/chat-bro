package com.weekendware.chatbro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weekendware.chatbro.ui.CheckInScreen

sealed class Screen(val route: String) {
    object CheckIn : Screen("check_in")
    object History : Screen("history")
}

@Composable
fun ChatBroNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.CheckIn.route) {
        composable(Screen.CheckIn.route) { CheckInScreen() }
        // Add more screens here
    }
}