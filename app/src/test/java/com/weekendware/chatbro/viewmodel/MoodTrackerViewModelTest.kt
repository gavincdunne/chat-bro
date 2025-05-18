package com.weekendware.chatbro.viewmodel

import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.repository.MoodRepository
import com.weekendware.chatbro.domain.model.MoodType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackerViewModelTest {

    private lateinit var viewModel: MoodTrackerViewModel
    private lateinit var repository: MoodRepository
    private lateinit var aiService: OpenAiService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = mockk(relaxed = true)
        aiService = mockk(relaxed = true)

        viewModel = MoodTrackerViewModel(repository, aiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selected mood updates currentMood`() = runTest {
        viewModel.selectMood(MoodType.HAPPY)
        assertEquals(MoodType.HAPPY, viewModel.currentMood.value)
    }

    @Test
    fun `addMoodEntry adds to repository and clears currentMood`() = runTest {
        coEvery {
            aiService.getMoodInsight("CALM", "some note")
        } returns "calming AI insight"

        viewModel.selectMood(MoodType.CALM)
        viewModel.addMoodEntry("some note")

        testScheduler.advanceUntilIdle()

        coVerify {
            repository.insertMood(match {
                it.mood == "CALM" &&
                        it.note == "some note" &&
                        it.insight == "calming AI insight"
            })
        }

        assertNull(viewModel.currentMood.value)
    }

    @Test
    fun `addMoodEntry does nothing if no mood selected`() = runTest {
        viewModel.addMoodEntry("Should not add")

        testScheduler.advanceUntilIdle()

        assertNull(viewModel.currentMood.value)
    }
}