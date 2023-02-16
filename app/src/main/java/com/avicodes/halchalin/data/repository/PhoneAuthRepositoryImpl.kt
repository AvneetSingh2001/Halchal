package com.avicodes.halchalin.data.repository

import com.avicodes.halchalin.data.repository.dataSource.PhoneAuthDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow

class PhoneAuthRepositoryImpl(
    private val phoneAuthDataSource: PhoneAuthDataSource
): PhoneAuthRepository {
    override val signUpState: MutableStateFlow<Result<String>>
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