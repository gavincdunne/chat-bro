package com.weekendware.chatbro.ui.moodtracker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.weekendware.chatbro.data.local.entity.MoodEntity
import com.weekendware.chatbro.viewmodel.MoodTrackerViewModel.MoodTrackerState
import formatPrettyTimestamp

@Composable
fun MoodHistorySection(
    state: MoodTrackerState,
    moodHistory: List<MoodEntity>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text("Mood History", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        if (moodHistory.isEmpty()) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "AI Insight: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
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
}

@Preview(showBackground = true)
@Composable
fun MoodHistorySectionPreview() {
    val sampleData = listOf(
        MoodEntity(
            mood = "Happy",
            note = "Had a great walk in the sun",
            insight = "You're thriving — keep it up!",
            timestamp = System.currentTimeMillis()
        ),
        MoodEntity(
            mood = "Sad",
            note = "Didn't sleep well",
            insight = "Try to get some rest tonight.",
            timestamp = System.currentTimeMillis() - 86_400_000
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        MoodHistorySection(
            state = MoodTrackerState.Idle,
            moodHistory = sampleData
        )
    }
}