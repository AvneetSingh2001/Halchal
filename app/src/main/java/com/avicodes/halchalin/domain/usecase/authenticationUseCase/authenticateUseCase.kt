package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.domain.repository.PhoneAuthRepository

class authenticateUseCase(
    private val phoneAuthRepository: PhoneAuthRepository
) {

    suspend fun execute(phone: String) = phoneAuthRepository.authenticate(phone)
}