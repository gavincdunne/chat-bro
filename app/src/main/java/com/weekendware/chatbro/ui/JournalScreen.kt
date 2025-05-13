package com.weekendware.chatbro.ui

import androidx.compose.runtime.Composable
import com.weekendware.chatbro.viewmodel.JournalViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun JournalScreen(viewModel: JournalViewModel = koinViewModel()) {}