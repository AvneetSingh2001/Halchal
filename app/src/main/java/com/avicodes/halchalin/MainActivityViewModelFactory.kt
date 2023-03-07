package com.avicodes.halchalin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.UserRespository
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel

class MainActivityViewModelFactory (
    private val userRespository: UserRespository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            userRespository
        ) as T
    }
}