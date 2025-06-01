package com.weekendware.chatbro.data.remote.core

@Suppress("SerializableObject")
sealed class AppError : Throwable() {
    object Network : AppError()
    object Timeout : AppError()
    object Unauthorized : AppError()
    object RateLimited : AppError()
    data class Unknown(val original: Throwable) : AppError()
}


fun AppError.toUserMessage(): String = when (this) {
    is AppError.Network -> "No internet connection."
    is AppError.Timeout -> "Request timed out."
    is AppError.Unauthorized -> "You're not authorized."
    is AppError.RateLimited -> "Rate limit hit. Please try again later."
    is AppError.Unknown -> {
        original.printStackTrace()
        "Something went wrong."
    }
}