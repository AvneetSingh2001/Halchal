package com.avicodes.halchalin.presentation.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.domain.usecase.LoginUserUseCase
import com.avicodes.halchalin.domain.usecase.logoutUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthFragmentViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val logoutUserUseCase: logoutUserUseCase
): ViewModel() {

    fun loginUser(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUserUseCase.execute("+91${number}")
        }
    }



}