package com.avicodes.halchalin.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.authenticateUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.onVerifyOtpUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.signUpStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthFragmentViewModel(
    private val authenticateUseCase: authenticateUseCase,
    private val onVerifyOtpUseCase: onVerifyOtpUseCase,
    private val signUpStateUseCase: signUpStateUseCase,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    val signUpState: MutableStateFlow<Response<String>> = signUpStateUseCase.execute()

    fun authenticatePhone(phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authenticateUseCase.execute("+91$phone")
        }
    }

    fun verifyOtp(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            onVerifyOtpUseCase.execute(code)
        }
    }

    fun getUser(uid: String) = liveData<Response<User>> {
        getUserUseCase.execute(uid).collect { response ->
            emit(response)
        }
    }


}