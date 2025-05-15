package com.weekendware.chatbro.viewmodel

import com.weekendware.chatbro.domain.model.MoodType
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class MoodTrackerViewModelTest {

    private lateinit var viewModel: MoodTrackerViewModel

    @Before
    fun setUp() {
        viewModel = MoodTrackerViewModel()
    }

    @Test
    fun `selected mood updates currentMood`() = runTest {
        viewModel.selectMood(MoodType.HAPPY)
        assertEquals(MoodType.HAPPY, viewModel.currentMood.value)
    }

    @Test
    fun `addMoodEntry adds a new entry and clears currentMood`() = runTest {
        viewModel.selectMood(MoodType.SAD)
        viewModel.addMoodEntry("Feeling down today")

        val entries = viewModel.moodEntries.value
        assertEquals(1, entries.size)
        assertEquals(MoodType.SAD, entries[0].mood)
        assertEquals("Feeling down today", entries[0].note)
    }

    @Test
    fun `addMoodEntry does nothing if no mood selected`() = runTest {
        viewModel.addMoodEntry("Should not add")

        val entries = viewModel.moodEntries.value
        assertTrue(entries.isEmpty())
    }
}