package com.avicodes.halchalin.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.domain.repository.UserRespository
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserPhoneUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.UserUploadRemotelyUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseUserMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsFragmentViewModel(
    private val auth: FirebaseAuth,
    private val userUploadRemotelyUseCase: UserUploadRemotelyUseCase,
    private val userRespository: UserRespository
): ViewModel() {

    fun saveUser(
        name: String,
        loc: String,
        phone: String,
    ) {
        val user = User(
            name = name,
            mobile = "$phone",
            location = loc,
            userId = phone
        )
        userRespository.saveUser(user)
    }

    fun login() = viewModelScope.launch(Dispatchers.IO){
        userRespository.login()
    }

}