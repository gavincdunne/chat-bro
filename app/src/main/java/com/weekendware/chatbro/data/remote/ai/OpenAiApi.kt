package com.weekendware.chatbro.data.remote.ai

import com.weekendware.chatbro.data.remote.model.ChatRequest
import com.weekendware.chatbro.data.remote.model.ChatResponse

interface OpenAiApi {
    @retrofit2.http.Headers("Content-Type: application/json")
    @retrofit2.http.POST("chat/completions")
    suspend fun chat(@retrofit2.http.Body request: ChatRequest): ChatResponse
}