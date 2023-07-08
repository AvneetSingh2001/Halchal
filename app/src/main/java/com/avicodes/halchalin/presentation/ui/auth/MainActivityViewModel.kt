package com.avicodes.halchalin.presentation.ui.auth

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.PhoneAuthRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val phoneAuthRepository: PhoneAuthRepository,
    private val userRespository: UserRespository,
) : ViewModel() {

    val phoneState: MutableStateFlow<Result<String>> = phoneAuthRepository.phoneState

    val codeState: MutableStateFlow<Result<String>> = phoneAuthRepository.codeState

    val curUser: MutableLiveData<User> = MutableLiveData()

    fun authenticatePhone(phone: String, activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            phoneAuthRepository.authenticate(phone, activity = activity)
        }
    }
    fun verifyOtp(code: String, activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            phoneAuthRepository.onVerifyOtp(code, activity)
        }
    }

    fun getUser(uid: String) = liveData<Result<User>> {
        userRespository.getUserRemotely(uid).collectLatest { response ->
            emit(response)
        }
    }

    fun isLoggedIn() : Flow<Boolean> {
        return userRespository.isLoggedIn()
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
        imgUrl: String,
        about: String
    ) = viewModelScope.launch(Dispatchers.IO){
        val user = User(
            userId = userId,
            name = name,
            location = loc,
            imgUrl =  imgUrl,
            about = about
        )
        userRespository.saveUserLocally(user)
    }

}