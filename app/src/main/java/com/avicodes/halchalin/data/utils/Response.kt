package com.avicodes.halchalin.data.utils


sealed class Response<out T>(
) {
    object NotInitialized : Response<Nothing>()
    class Loading(val message: String?) : Response<Nothing>()
    class Success<out T>(val data: T?) : Response<T>()
    class Error(val exception: Throwable?) : Response<Nothing>()
}