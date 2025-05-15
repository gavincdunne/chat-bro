package com.weekendware.chatbro.data.remote.ai

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class Message(val role: String, val content: String)
data class ChatRequest(val model: String, val messages: List<Message>)
data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Message)

interface OpenAiApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun chat(@Body request: ChatRequest): ChatResponse
}

class OpenAiService(apiKey: String) {
    private val api: OpenAiApi

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(OpenAiApi::class.java)
    }

    suspend fun getMoodInsight(mood: String, note: String?): String {
        val prompt = buildPrompt(mood, note)
        val request = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(Message("user", prompt))
        )
        val response = api.chat(request)
        return response.choices.firstOrNull()?.message?.content ?: "No insight available."
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