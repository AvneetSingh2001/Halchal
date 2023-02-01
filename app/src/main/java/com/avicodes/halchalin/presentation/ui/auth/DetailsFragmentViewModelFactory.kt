package com.avicodes.halchalin.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserPhoneUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.UserUploadRemotelyUseCase

class DetailsFragmentViewModelFactory(
    private val userUploadRemotelyUseCase: UserUploadRemotelyUseCase,
    private val getUserPhoneUseCase: GetUserPhoneUseCase
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsFragmentViewModel(
            userUploadRemotelyUseCase = userUploadRemotelyUseCase,
            getUserPhoneUseCase = getUserPhoneUseCase
            ) as T
    }
}