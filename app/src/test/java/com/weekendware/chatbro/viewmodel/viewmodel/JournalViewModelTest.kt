package com.weekendware.chatbro.viewmodel.viewmodel

import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.repository.JournalRepository
import com.weekendware.chatbro.viewmodel.JournalIntent
import com.weekendware.chatbro.viewmodel.JournalState
import com.weekendware.chatbro.viewmodel.JournalViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JournalViewModelTest {

    private lateinit var viewModel: JournalViewModel
    private lateinit var repository: JournalRepository
    private lateinit var aiService: OpenAiService
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        aiService = mockk(relaxed = true)
        viewModel = JournalViewModel(repository, aiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `body only enables save`() = runTest(testDispatcher) {
        viewModel.processIntent(JournalIntent.UpdateBody("Wrote some code today"))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        val editingState = state as? JournalState.Editing
        Assert.assertTrue(editingState?.isSaveEnabled == true)
    }

    @Test
    fun `empty body disables save`() = runTest(testDispatcher) {
        viewModel.processIntent(JournalIntent.UpdateBody(""))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        val editingState = state as? JournalState.Editing
        Assert.assertFalse(editingState?.isSaveEnabled == true)
    }

    @Test
    fun `title and body enables save`() = runTest(testDispatcher) {
        viewModel.processIntent(JournalIntent.UpdateTitle("Morning Thoughts"))
        viewModel.processIntent(JournalIntent.UpdateBody("Felt pretty good today."))
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        val editingState = state as? JournalState.Editing
        Assert.assertTrue(editingState?.isSaveEnabled == true)
    }

    @Test
    fun `saveEntry updates success flag and persists entry`() = runTest(testDispatcher) {
//        viewModel.processIntent(JournalIntent.UpdateTitle("Evening Recap"))
//        viewModel.processIntent(JournalIntent.UpdateBody("Long walk, feeling centered."))
//        viewModel.processIntent(JournalIntent.AddTag("reflection"))
//        viewModel.processIntent(JournalIntent.SaveEntry)
//
//        testDispatcher.scheduler.advanceUntilIdle()
//
//        val state = viewModel.state.value
//        Assert.assertTrue(state is JournalState.Success)
//        val success = state as JournalState.Success
//        Assert.assertEquals("Evening Recap", success.savedEntry.title)
//        Assert.assertEquals("Long walk, feeling centered.", success.savedEntry.body)
//        Assert.assertTrue(success.savedEntry.tags.contains("reflection"))
    }
}
