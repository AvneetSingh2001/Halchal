package com.avicodes.halchalin.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserPhoneUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.UserUploadRemotelyUseCase
import com.avicodes.halchalin.presentation.ui.auth.DetailsFragmentViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeActivityViewModelFactory(
    val auth: FirebaseAuth
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(
            auth
        ) as T
    }
}