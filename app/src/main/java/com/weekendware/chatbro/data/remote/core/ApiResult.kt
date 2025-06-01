package com.weekendware.chatbro.data.remote.core

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Failure(val throwable: Throwable) : ApiResult<Nothing>()
}