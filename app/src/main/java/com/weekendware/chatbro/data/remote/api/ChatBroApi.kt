
package com.weekendware.chatbro.data.remote.api

import retrofit2.http.GET

interface ChatBroApi {
    @GET("status")
    suspend fun checkStatus(): String
}
