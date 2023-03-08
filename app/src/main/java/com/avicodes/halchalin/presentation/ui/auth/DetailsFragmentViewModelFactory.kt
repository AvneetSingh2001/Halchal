package com.avicodes.halchalin.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.CityRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserPhoneUseCase
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.UserUploadRemotelyUseCase
import com.google.firebase.auth.FirebaseAuth

class DetailsFragmentViewModelFactory(
    private val auth: FirebaseAuth,
    private val userUploadRemotelyUseCase: UserUploadRemotelyUseCase,
    private val userRespository: UserRespository,
    private val cityRepository: CityRepository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsFragmentViewModel(
            auth = auth,
            userUploadRemotelyUseCase = userUploadRemotelyUseCase,
            userRespository = userRespository,
            cityRepository = cityRepository
            ) as T
    }
}