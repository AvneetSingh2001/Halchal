package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow

class signUpStateUseCase(
    private val phoneAuthRepository: PhoneAuthRepository
) {
    fun execute(): MutableStateFlow<Response> {
        return phoneAuthRepository.signUpState
    }
}