package com.avicodes.halchalin.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.*
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.*
import com.google.firebase.auth.FirebaseAuth

class HomeActivityViewModelFactory(
    val auth: FirebaseAuth,
    private val remoteNewsRepository: RemoteNewsRepository,
    private val localNewsRepository: LocalNewsRepository,
    val adsRepository: AdsRepository,
    val updateUserPicUseCase: updateUserPicUseCase,
    val userRespository: UserRespository,
    val cityRepository: CityRepository,
    val articleRepository: ArticleRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(
            auth,
            remoteNewsRepository,
            localNewsRepository,
            adsRepository,
            updateUserPicUseCase,
            userRespository,
            cityRepository,
            articleRepository
        ) as T
    }
}