package com.avicodes.halchalin.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.*
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.*
import com.google.firebase.auth.FirebaseAuth

class HomeActivityViewModelFactory(
    val auth: FirebaseAuth,
    private val categoryNewsRepository: CategoryNewsRepository,
    private val internationalNewsRepository: InternationalNewsRepository,
    private val localNewsRepository: LocalNewsRepository,
    private val nationalNewsRepository: NationalNewsRepository,
    val adsRepository: AdsRepository,
    val getUserByIdUseCase: GetUserByIdUseCase,
    val updateUserPicUseCase: updateUserPicUseCase,
    val userRespository: UserRespository,
    val cityRepository: CityRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(
            auth,
            categoryNewsRepository,
            internationalNewsRepository,
            localNewsRepository,
            nationalNewsRepository,
            adsRepository,
            getUserByIdUseCase,
            updateUserPicUseCase,
            userRespository,
            cityRepository
        ) as T
    }
}