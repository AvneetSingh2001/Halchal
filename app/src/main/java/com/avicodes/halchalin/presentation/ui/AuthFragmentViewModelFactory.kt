package com.avicodes.halchalin.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.usecase.LoginUserUseCase
import com.avicodes.halchalin.domain.usecase.logoutUserUseCase

class AuthFragmentViewModelFactory(
    private val loginUserUseCase: LoginUserUseCase,
    private val logoutUserUseCase: logoutUserUseCase
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthFragmentViewModel(loginUserUseCase, logoutUserUseCase) as T
    }
}
