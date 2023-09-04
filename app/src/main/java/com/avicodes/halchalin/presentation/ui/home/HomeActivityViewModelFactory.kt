package com.avicodes.halchalin.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.*
import com.google.firebase.auth.FirebaseAuth

class HomeActivityViewModelFactory(
    val auth: FirebaseAuth,
    private val remoteNewsRepository: RemoteNewsRepository,
    private val localNewsRepository: LocalNewsRepository,
    val adsRepository: AdsRepository,
    val userRespository: UserRespository,
    val cityRepository: CityRepository,
    val articleRepository: ArticleRepository,
    val adminRepository: AdminRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(
            auth,
            remoteNewsRepository,
            localNewsRepository,
            adsRepository,
            userRespository,
            cityRepository,
            articleRepository,
            adminRepository
        ) as T
    }
}