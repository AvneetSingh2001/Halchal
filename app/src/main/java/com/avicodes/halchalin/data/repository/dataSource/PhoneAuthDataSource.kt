package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.utils.Response
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow

interface PhoneAuthDataSource {
    val signUpState: MutableStateFlow<Response>

    fun authenticate(phone: String)

    fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    )

    fun onVerifyOtp(code: String)

    fun onVerificationCompleted(
        credential: PhoneAuthCredential
    )

    fun onVerificationFailed(exception: Exception)

    fun getUserPhone() : String
}