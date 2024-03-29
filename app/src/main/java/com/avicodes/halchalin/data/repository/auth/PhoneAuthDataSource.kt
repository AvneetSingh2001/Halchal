package com.avicodes.halchalin.data.repository.auth

import android.app.Activity
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow

interface PhoneAuthDataSource {

    val phoneState: MutableStateFlow<Result<String>>
    val codeState: MutableStateFlow<Result<String>>

    suspend fun authenticate(phone: String, activity: Activity)

    fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    )

    suspend fun onVerifyOtp(code: String, activity: Activity)

    fun onVerificationCompleted(
        credential: PhoneAuthCredential
    )

    fun onVerificationFailed(exception: Exception)

    fun getUserPhone() : String
}