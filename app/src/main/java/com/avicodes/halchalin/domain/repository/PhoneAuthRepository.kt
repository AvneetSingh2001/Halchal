package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.utils.Response
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow

interface PhoneAuthRepository {
    val signUpState: MutableStateFlow<Response>

    suspend fun authenticate(phone: String)

    suspend fun onVerifyOtp(code: String)

    fun getUserPhone() : String
}