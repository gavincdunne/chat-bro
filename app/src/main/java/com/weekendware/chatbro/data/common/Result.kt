package com.weekendware.chatbro.data.common

sealed class Result<out T> {
    data class Success<T>(val data: T): Result<T>()
    data class Error(val message: String, val throwable: Throwable? = null): Result<Nothing>()
    object Loading : Result<Nothing>()
}