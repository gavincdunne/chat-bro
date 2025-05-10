package com.weekendware.chatbro.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weekendware.chatbro.viewmodel.CheckInViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CheckInScreen(viewModel: CheckInViewModel = koinViewModel()) {
    var mood by remember { mutableStateOf("🙂") }
    var journal by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("How are you feeling today?", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Selected mood: $mood", style = MaterialTheme.typography.bodyLarge)

        OutlinedTextField(
            value = journal,
            onValueChange = { journal = it },
            label = { Text("Journal Entry") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Button(
            onClick = {
                viewModel.saveMood(mood, journal)
                journal = ""
            }
        ) {
            Text("Save Check-In")
        }
    }
}
