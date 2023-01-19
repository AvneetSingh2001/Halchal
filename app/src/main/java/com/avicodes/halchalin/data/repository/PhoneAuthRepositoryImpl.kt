package com.avicodes.halchalin.data.repository

import com.avicodes.halchalin.data.repository.dataSource.PhoneAuthDataSource
import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow

class PhoneAuthRepositoryImpl(
    private val phoneAuthDataSource: PhoneAuthDataSource
): PhoneAuthRepository {
    override val signUpState: MutableStateFlow<Response>
        get() = phoneAuthDataSource.signUpState

    override suspend fun authenticate(phone: String) {
        phoneAuthDataSource.authenticate(phone)
    }

    override suspend fun onVerifyOtp(code: String) {
        phoneAuthDataSource.onVerifyOtp(code)
    }

    override fun getUserPhone(): String {
        return phoneAuthDataSource.getUserPhone()
    }
}