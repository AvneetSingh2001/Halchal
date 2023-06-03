package com.avicodes.halchalin

import androidx.lifecycle.ViewModel
import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.flow.Flow

class SplashViewModel(
    private val userRespository: UserRespository,
) : ViewModel() {
    fun isLoggedIn() : Flow<Boolean> {
        return userRespository.isLoggedIn()
    }
}