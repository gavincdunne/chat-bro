package com.weekendware.chatbro.data.remote.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class ApiService<T>(
    private val baseUrl: String,
    private val apiKey: String,
    private val apiClass: Class<T>
) {
    protected val api: T by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(createAuthInterceptor())
            .addInterceptor(createLoggingInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(apiClass)
    }

    open fun createAuthInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        chain.proceed(request)
    }

    open fun createLoggingInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    private val mutex = Mutex()

    protected suspend fun <R> safeApiCall(block: suspend () -> R): ApiResult<R> {
        return mutex.withLock {
            try {
                val result = withContext(Dispatchers.IO) { block() }
                ApiResult.Success(result)
            } catch (e: Exception) {
                ApiResult.Failure(e)
            }
        }
    }

    open fun mapToAppError(e: Throwable): AppError {
        return when (e) {
            is java.net.UnknownHostException -> AppError.Network
            is java.net.SocketTimeoutException -> AppError.Timeout
            is java.io.IOException -> AppError.Network

            is retrofit2.HttpException -> when (e.code()) {
                401 -> AppError.Unauthorized
                429 -> AppError.RateLimited
                else -> AppError.Unknown(e)
            }

            else -> AppError.Unknown(e)
        }
    }
}
