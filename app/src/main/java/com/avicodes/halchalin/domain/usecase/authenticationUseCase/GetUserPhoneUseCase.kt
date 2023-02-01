package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.domain.repository.PhoneAuthRepository

class GetUserPhoneUseCase(
    private val phoneAuthRepository: PhoneAuthRepository
) {
    fun execute() = phoneAuthRepository.getUserPhone()
}