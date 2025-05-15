package com.weekendware.chatbro.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.weekendware.chatbro.domain.model.MoodType
import com.weekendware.chatbro.viewmodel.MoodTrackerViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.*


@Composable
fun MoodTrackerScreen(viewModel: MoodTrackerViewModel = koinViewModel()) {
    val currentMood by viewModel.currentMood.collectAsState()
    val moodOptions = MoodType.entries.toList()

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
    }
}