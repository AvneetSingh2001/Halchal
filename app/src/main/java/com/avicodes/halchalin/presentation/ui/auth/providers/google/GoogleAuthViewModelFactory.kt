package com.avicodes.halchalin.presentation.ui.auth.providers.google

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.UserRespository

class GoogleAuthViewModelFactory(
    private val userRespository: UserRespository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GoogleAuthActivityViewModel(
            userRespository = userRespository
        ) as T
    }
}