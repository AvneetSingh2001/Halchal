package com.avicodes.halchalin.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.authenticateUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.onVerifyOtpUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.signUpStateUseCase

class AuthFragmentViewModelFactory(
    private val authenticateUseCase: authenticateUseCase,
    private val onVerifyOtpUseCase: onVerifyOtpUseCase,
    private val signUpStateUseCase: signUpStateUseCase
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthFragmentViewModel(authenticateUseCase, onVerifyOtpUseCase, signUpStateUseCase) as T
    }
}
