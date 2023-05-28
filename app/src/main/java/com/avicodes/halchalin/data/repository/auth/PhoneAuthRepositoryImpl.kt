package com.avicodes.halchalin.data.repository.auth

import android.app.Activity
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow

class PhoneAuthRepositoryImpl(
    private val phoneAuthDataSource: PhoneAuthDataSource
): PhoneAuthRepository {
    override val phoneState: MutableStateFlow<Result<String>>
        get() = phoneAuthDataSource.phoneState

    override val codeState: MutableStateFlow<Result<String>>
        get() = phoneAuthDataSource.codeState

    override suspend fun authenticate(phone: String, activity: Activity) {
        phoneAuthDataSource.authenticate(phone, activity)
    }

    override suspend fun onVerifyOtp(code: String, activity: Activity) {
        phoneAuthDataSource.onVerifyOtp(code, activity)
    }

    override fun getUserPhone(): String {
        return phoneAuthDataSource.getUserPhone()
    }
}