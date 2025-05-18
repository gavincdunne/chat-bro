package com.weekendware.chatbro.viewmodel

import com.weekendware.chatbro.data.common.Result
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
import org.junit.Assert.assertTrue
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
    fun `addMoodEntry adds to repository and updates aiInsightState`() = runTest {
        val insightText = "calming AI insight"
        coEvery { aiService.getMoodInsight("CALM", "some note") } returns Result.Success(insightText)

        viewModel.selectMood(MoodType.CALM)
        viewModel.addMoodEntry("some note")

        testScheduler.advanceUntilIdle()

        // Verify repository insert
        coVerify {
            repository.insertMood(match {
                it.mood == "CALM" &&
                        it.note == "some note" &&
                        it.insight == insightText
            })
        }

        // Verify current mood is cleared
        assertNull(viewModel.currentMood.value)

        val result = viewModel.aiInsightState.value
        assertTrue(result is Result.Success)
        assertEquals(insightText, (result as Result.Success).data)
    }

    @Test
    fun `addMoodEntry sets aiInsightState to Error if service fails`() = runTest {
        coEvery { aiService.getMoodInsight("SAD", any()) } returns Result.Error("AI insight unavailable (rate limit hit).")

        viewModel.selectMood(MoodType.SAD)
        viewModel.addMoodEntry("")

        testScheduler.advanceUntilIdle()

        val result = viewModel.aiInsightState.value
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).message.contains("rate limit hit"))
    }

    @Test
    fun `addMoodEntry does nothing if no mood selected`() = runTest {
        viewModel.addMoodEntry("Should not add")
        testScheduler.advanceUntilIdle()
        assertNull(viewModel.currentMood.value)
    }
}
