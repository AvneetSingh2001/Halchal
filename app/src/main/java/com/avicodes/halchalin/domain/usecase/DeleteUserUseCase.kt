package com.avicodes.halchalin.domain.usecase

import com.avicodes.halchalin.domain.repository.AuthRepository

class DeleteUserUseCase(private val authRepository: AuthRepository) {
    suspend fun execute(contact: String) {
        authRepository.deleteUser(contact)
    }
}