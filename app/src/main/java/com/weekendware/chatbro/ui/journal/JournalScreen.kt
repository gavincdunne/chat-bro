package com.weekendware.chatbro.ui.journal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weekendware.chatbro.viewmodel.*
import formatPrettyTimestamp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun JournalScreen(viewModel: JournalViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val editing = state as? JournalState.Editing

    LaunchedEffect(state) {
        when (state) {
            is JournalState.Success -> {
                val entry = (state as JournalState.Success).savedEntry
                snackbarHostState.showSnackbar("Saved journal at ${formatPrettyTimestamp(entry.timestamp)}")
                delay(1500)
                viewModel.processIntent(JournalIntent.Reset)
            }
            is JournalState.Error -> {
                val message = (state as JournalState.Error).exception.message ?: "Something went wrong."
                snackbarHostState.showSnackbar("Error: $message")
                delay(1500)
                viewModel.processIntent(JournalIntent.Reset)
            }
            else -> Unit
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = editing?.body.orEmpty(),
                onValueChange = { viewModel.processIntent(JournalIntent.UpdateBody(it)) },
                label = { Text("Write your journal entry") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.processIntent(JournalIntent.SaveEntry) },
                enabled = editing?.isSaveEnabled == true,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
            }
        }
    }
}
