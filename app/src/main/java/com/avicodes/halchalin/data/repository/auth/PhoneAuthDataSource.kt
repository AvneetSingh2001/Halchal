package com.avicodes.halchalin.data.repository.auth

import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow

interface PhoneAuthDataSource {
    val signUpState: MutableStateFlow<Result<String>>

    suspend fun authenticate(phone: String)

    fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    )

    suspend fun onVerifyOtp(code: String)

    fun onVerificationCompleted(
        credential: PhoneAuthCredential
    )

    fun onVerificationFailed(exception: Exception)

    fun getUserPhone() : String
}