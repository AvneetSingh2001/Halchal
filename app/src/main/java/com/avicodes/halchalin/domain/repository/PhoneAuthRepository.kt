package com.avicodes.halchalin.domain.repository

import android.app.Activity
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface PhoneAuthRepository {

    val phoneState: MutableStateFlow<Result<String>>
    val codeState: MutableStateFlow<Result<String>>

    suspend fun authenticate(phone: String,activity: Activity)

    suspend fun onVerifyOtp(code: String, activity: Activity)

    fun getUserPhone(): String
}