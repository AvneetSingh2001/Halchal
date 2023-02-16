package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface PhoneAuthRepository {
    val signUpState: MutableStateFlow<Result<String>>

    suspend fun authenticate(phone: String)

    suspend fun onVerifyOtp(code: String)

    fun getUserPhone() : String
}