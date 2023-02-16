package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
import kotlinx.coroutines.flow.MutableStateFlow

class signUpStateUseCase(
    private val phoneAuthRepository: PhoneAuthRepository
) {
    fun execute(): MutableStateFlow<Result<String>> {
        return phoneAuthRepository.signUpState
    }
}