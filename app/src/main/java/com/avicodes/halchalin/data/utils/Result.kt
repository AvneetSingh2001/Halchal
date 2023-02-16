package com.avicodes.halchalin.data.utils


sealed class Result<out T>(
) {
    object NotInitialized : Result<Nothing>()
    class Loading(val message: String?) : Result<Nothing>()
    class Success<out T>(val data: T?) : Result<T>()
    class Error(val exception: Throwable?) : Result<Nothing>()
}