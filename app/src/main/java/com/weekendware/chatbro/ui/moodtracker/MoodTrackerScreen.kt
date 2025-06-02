package com.weekendware.chatbro.ui.moodtracker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.weekendware.chatbro.domain.model.MoodType
import com.weekendware.chatbro.viewmodel.*
import formatPrettyTimestamp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoodTrackerScreen(viewModel: MoodTrackerViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val moodHistory by viewModel.moodHistory.collectAsState()
    val moodOptions = MoodType.entries.toList()

    val selected = state as? MoodTrackerState.MoodSelected
    val note = selected?.note.orEmpty()
    val selectedMood = selected?.mood

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        when (state) {
            is MoodTrackerState.Success -> {
                val entry = (state as MoodTrackerState.Success).newEntry
                snackbarHostState.showSnackbar("Saved ${entry.mood} at ${formatPrettyTimestamp(entry.timestamp)}")
                delay(1500)
                viewModel.processIntent(MoodIntent.Reset)
            }
            is MoodTrackerState.Error -> {
                val message = (state as MoodTrackerState.Error).exception.message ?: "Something went wrong."
                snackbarHostState.showSnackbar("Error: $message")
                delay(1500)
                viewModel.processIntent(MoodIntent.Reset)
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
            MoodHeaderSection(
                moodOptions = moodOptions,
                selectedMood = selectedMood,
                onMoodSelected = { viewModel.processIntent(MoodIntent.SelectMood(it)) }
            )

            Spacer(Modifier.height(16.dp))

            MoodInputSection(
                note = note,
                onNoteChange = { viewModel.processIntent(MoodIntent.UpdateNote(it)) },
                onSave = { viewModel.processIntent(MoodIntent.SaveMood) },
                isEnabled = selected != null
            )

            Spacer(Modifier.height(24.dp))

            MoodHistorySection(moodHistory = moodHistory)
        }
    }
}