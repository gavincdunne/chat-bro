package com.weekendware.chatbro.viewmodel

import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.remote.core.ApiResult
import com.weekendware.chatbro.data.repository.MoodRepository
import com.weekendware.chatbro.domain.model.MoodType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoodTrackerViewModelTest {

    private lateinit var viewModel: MoodTrackerViewModel
    private lateinit var repository: MoodRepository
    private lateinit var aiService: OpenAiService
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

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
    fun `selectMood updates state to MoodSelected`() = runTest(testDispatcher) {
        viewModel.processIntent(MoodIntent.SelectMood(MoodType.HAPPY))
        testScheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is MoodTrackerState.MoodSelected)
        assertEquals(MoodType.HAPPY, (state as MoodTrackerState.MoodSelected).mood)
    }

    @Test
    fun `saveMood adds entry and sets state to Success`() = runTest(testDispatcher) {
        coEvery {
            aiService.getMoodInsight("CALM", "some note")
        } returns ApiResult.Success("calming AI insight")

        viewModel.processIntent(MoodIntent.SelectMood(MoodType.CALM))
        testScheduler.advanceUntilIdle()

        viewModel.processIntent(MoodIntent.UpdateNote("some note"))
        testScheduler.advanceUntilIdle()

        viewModel.processIntent(MoodIntent.SaveMood)
        testScheduler.advanceUntilIdle()

        coVerify {
            repository.insertMood(match {
                it.mood == "CALM" &&
                        it.note == "some note" &&
                        it.insight == "calming AI insight"
            })
        }

        val state = viewModel.state.value
        assertTrue(state is MoodTrackerState.Success)
    }

    @Test
    fun `saveMood does nothing if mood not selected`() = runTest(testDispatcher) {
        viewModel.processIntent(MoodIntent.SaveMood)
        testScheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is MoodTrackerState.Idle)
    }

    @Test
    fun `updateNote updates note if mood is selected`() = runTest(testDispatcher) {
        viewModel.processIntent(MoodIntent.SelectMood(MoodType.SAD))
        viewModel.processIntent(MoodIntent.UpdateNote("updated note"))
        testScheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is MoodTrackerState.MoodSelected)
        assertEquals("updated note", (state as MoodTrackerState.MoodSelected).note)
    }

    @Test
    fun `updateNote does nothing if mood is not selected`() = runTest(testDispatcher) {
        viewModel.processIntent(MoodIntent.UpdateNote("should not apply"))
        testScheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state is MoodTrackerState.MoodSelected)
    }
}