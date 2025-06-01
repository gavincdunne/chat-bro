package com.weekendware.chatbro.ui.moodtracker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MoodInputSection(
    note: String,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    isEnabled: Boolean
) {
    OutlinedTextField(
        value = note,
        onValueChange = onNoteChange,
        label = { Text("Optional note") },
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled
    )

    Spacer(Modifier.height(16.dp))

    Button(onClick = onSave, enabled = isEnabled) {
        Text("Save Mood")
    }
}

@Preview(showBackground = true)
@Composable
fun MoodInputSectionPreview() {
    MoodInputSection(
        note = "Feeling pretty good today!",
        onNoteChange = {},
        onSave = {},
        isEnabled = true
    )
}
