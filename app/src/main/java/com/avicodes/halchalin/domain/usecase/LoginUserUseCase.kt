package com.avicodes.halchalin.domain.usecase

import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class LoginUserUseCase(private val authRepository : AuthRepository) {
    suspend fun execute(contact: String) : Response {
        return authRepository.login(contact)
    }
}