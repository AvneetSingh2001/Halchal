package com.avicodes.halchalin.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.AdsRepository
import com.avicodes.halchalin.domain.repository.NewsRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.*
import com.avicodes.halchalin.presentation.ui.auth.DetailsFragmentViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeActivityViewModelFactory(
    val auth: FirebaseAuth,
    val newsRepository: NewsRepository,
    val adsRepository: AdsRepository,
    val getUserByIdUseCase: GetUserByIdUseCase,
    val updateUserPicUseCase: updateUserPicUseCase,
    val userRespository: UserRespository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(
            auth,
            newsRepository,
            adsRepository,
            getUserByIdUseCase,
            updateUserPicUseCase,
            userRespository
        ) as T
    }
}