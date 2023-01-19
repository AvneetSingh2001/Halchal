package com.avicodes.halchalin.domain.usecase

import com.avicodes.halchalin.domain.repository.PhoneAuthRepository

class getUserPhoneUseCase(
    private val phoneAuthRepository: PhoneAuthRepository
) {
    fun execute() = phoneAuthRepository.getUserPhone()
}