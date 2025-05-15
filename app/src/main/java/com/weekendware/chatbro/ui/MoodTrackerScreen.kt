package com.weekendware.chatbro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.weekendware.chatbro.domain.model.MoodType
import com.weekendware.chatbro.viewmodel.MoodTrackerViewModel
import formatPrettyTimestamp
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoodTrackerScreen(viewModel: MoodTrackerViewModel = koinViewModel()) {
    val currentMood by viewModel.currentMood.collectAsState()
    val moodOptions = MoodType.entries.toList()
    val moodHistory by viewModel.moodHistory.collectAsState()
    var note by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("How are you feeling today?", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow {
            items(moodOptions) { mood ->
                val isSelected = mood == currentMood
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(64.dp)
                        .background(
                            if (isSelected) Color.LightGray else Color.Transparent,
                            shape = MaterialTheme.shapes.medium
                        )
                        .clickable { viewModel.selectMood(mood) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = mood.emoji, style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Optional note") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.addMoodEntry(note)
                note = ""
            },
            enabled = currentMood != null
        ) {
            Text("Save Mood")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Mood History", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        ) {
            itemsIndexed(moodHistory) { index, entry ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 12.dp)
                ) {
                    Text(entry.mood, style = MaterialTheme.typography.bodyLarge)

                    entry.note?.takeIf { it.isNotBlank() }?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }

                    Text(
                        formatPrettyTimestamp(entry.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    entry.insight?.takeIf { it.isNotBlank() }?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "AI Insight: $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(MaterialTheme.colorScheme.secondary.value)
                        )
                    }
                }

                if (index < moodHistory.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                }
            }
        }
    }
}
