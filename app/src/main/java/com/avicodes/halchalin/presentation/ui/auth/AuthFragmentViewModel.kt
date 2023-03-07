package com.avicodes.halchalin.presentation.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.UserRespository
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.authenticateUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.onVerifyOtpUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.signUpStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthFragmentViewModel(
    private val authenticateUseCase: authenticateUseCase,
    private val onVerifyOtpUseCase: onVerifyOtpUseCase,
    private val signUpStateUseCase: signUpStateUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val userRespository: UserRespository
): ViewModel() {

    val signUpState: MutableStateFlow<Result<String>> = signUpStateUseCase.execute()
    val curUser: MutableLiveData<User> = MutableLiveData()

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
    fun getUser(uid: String) = liveData<Result<User>> {
        getUserUseCase.execute(uid).collectLatest { response ->
            emit(response)
        }
    }

    fun isLoggedIn() = liveData {
        userRespository.isLoggedIn().collectLatest {
            emit(it)
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRespository.getUserLocally().collectLatest {
                curUser.postValue(it)
            }
        }
    }

    fun login() = viewModelScope.launch(Dispatchers.IO){
        userRespository.login()
    }

    fun saveUser(
        userId: String,
        name: String,
        loc: String,
        phone: String,
        imgUrl: String,
        about: String
    ) = viewModelScope.launch(Dispatchers.IO){
        val user = User(
            userId = userId,
            name = name,
            mobile = "$phone",
            location = loc,
            imgUrl =  imgUrl,
            about = about
        )
        userRespository.saveUserLocally(user)
    }
}