package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.repository.dataSource.PhoneAuthDataSource
import com.avicodes.halchalin.data.utils.Response
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow

class PhoneAuthDataSourceImpl: PhoneAuthDataSource {
    override val signUpState: MutableStateFlow<Response>
        get() = TODO("Not yet implemented")

    override fun authenticate(phone: String) {
        TODO("Not yet implemented")
    }

    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
        TODO("Not yet implemented")
    }

    override fun onVerifyOtp(code: String) {
        TODO("Not yet implemented")
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        TODO("Not yet implemented")
    }

    override fun onVerificationFailed(exception: Exception) {
        TODO("Not yet implemented")
    }

    override fun getUserPhone(): String {
        TODO("Not yet implemented")
    }
}