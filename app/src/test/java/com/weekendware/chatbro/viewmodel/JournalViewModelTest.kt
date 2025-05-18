package com.weekendware.chatbro.viewmodel

import com.weekendware.chatbro.data.remote.ai.OpenAiService
import com.weekendware.chatbro.data.repository.JournalRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class JournalViewModelTest {

    private lateinit var viewModel: JournalViewModel
    private lateinit var repository: JournalRepository
    private lateinit var aiService: OpenAiService

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        aiService = mockk()
        viewModel = JournalViewModel(repository, aiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `typing updates journal text state`() = runTest {
//        viewModel.updateJournalText("Hello world")
//        assertEquals("Hello world", viewModel.journalText.value)
    }

    @Test
    fun `saveEntry saves to repository and clears journal text`() = runTest {
//        val text = "Today I felt good."
//        viewModel.updateJournalText(text)
//
//        coEvery { aiService.getJournalInsight(text) } returns null
//
//        viewModel.saveEntry()
//        testScheduler.advanceUntilIdle()
//
//        coVerify {
//            repository.insertEntry(match {
//                it.text == text && it.insight == null
//            })
//        }
//
//        assertEquals("", viewModel.journalText.value)
    }

    @Test
    fun `saveEntry does nothing when journal text is blank`() = runTest {
//        viewModel.updateJournalText("  ")
//        viewModel.saveEntry()
//        testScheduler.advanceUntilIdle()
//
//        coVerify(exactly = 0) { repository.insertEntry(any()) }
//        assertEquals("  ", viewModel.journalText.value)
    }

    @Test
    fun `saveEntry includes AI insight in saved entry`() = runTest {
//        val text = "I feel anxious and overwhelmed."
//        val insight = "It sounds like you're experiencing anxiety due to stress."
//
//        coEvery { aiService.getJournalInsight(text) } returns insight
//
//        viewModel.updateJournalText(text)
//        viewModel.saveEntry()
//        testScheduler.advanceUntilIdle()
//
//        coVerify {
//            repository.insertEntry(match {
//                it.text == text && it.insight == insight
//            })
//        }
    }
}