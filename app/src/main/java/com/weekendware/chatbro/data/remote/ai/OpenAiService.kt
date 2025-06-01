package com.weekendware.chatbro.data.remote.ai

import com.weekendware.chatbro.data.remote.core.ApiResult
import com.weekendware.chatbro.data.remote.model.ChatRequest
import com.weekendware.chatbro.data.remote.model.Message
import com.weekendware.chatbro.data.remote.core.ApiService


class OpenAiService(apiKey: String) :
    ApiService<OpenAiApi>(
        baseUrl = "https://api.openai.com/v1/",
        apiKey = apiKey,
        apiClass = OpenAiApi::class.java
    ) {

    suspend fun getMoodInsight(mood: String, note: String?): ApiResult<String> {
        val prompt = buildPrompt(mood, note)
        return safeApiCall {
            val response = api.chat(
                ChatRequest(
                    model = "gpt-3.5-turbo",
                    messages = listOf(Message("user", prompt))
                )
            )
            response.choices.firstOrNull()?.message?.content ?: "No insight available."
        }
    }

    private fun buildPrompt(mood: String, note: String?): String {
        return buildString {
            append("I'm building a mood tracking app. ")
            append("The user logged the mood \"$mood\".")
            if (!note.isNullOrBlank()) append(" They added: \"$note\".")
            append(" Give a brief and supportive insight.")
        }
    }
}
