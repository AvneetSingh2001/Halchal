package com.avicodes.halchalin.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.UserRespository

class SplashViewModelFactory (
    private val userRespository: UserRespository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(
            userRespository = userRespository
        ) as T
    }
}