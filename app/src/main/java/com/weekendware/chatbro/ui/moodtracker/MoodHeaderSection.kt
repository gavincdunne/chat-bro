package com.weekendware.chatbro.ui.moodtracker

import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weekendware.chatbro.domain.model.MoodType

@Composable
fun MoodHeaderSection(
    moodOptions: List<MoodType>,
    selectedMood: MoodType?,
    onMoodSelected: (MoodType) -> Unit
) {
    Text("How are you feeling today?", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(12.dp))

    LazyRow {
        items(moodOptions) { mood ->
            val isSelected = mood == selectedMood
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp)
                    .background(
                        if (isSelected) Color.LightGray else Color.Transparent,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { onMoodSelected(mood) },
                contentAlignment = Alignment.Center
            ) {
                Text(mood.emoji, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoodHeaderSectionPreview() {
    MoodHeaderSection(
        moodOptions = MoodType.entries.toList(),
        selectedMood = MoodType.HAPPY,
        onMoodSelected = {}
    )
}

