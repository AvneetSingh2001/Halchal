package com.avicodes.halchalin.domain.usecase

import com.avicodes.halchalin.domain.repository.AuthRepository

class logoutUserUseCase(private val authRepository: AuthRepository) {
    suspend fun execute() {
        authRepository.logout()
    }
}