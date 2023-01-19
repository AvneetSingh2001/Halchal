package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.domain.repository.PhoneAuthRepository

class onVerifyOtpUseCase(private val phoneAuthRepository: PhoneAuthRepository) {

    suspend fun execute(code: String) = phoneAuthRepository.onVerifyOtp(code)
}