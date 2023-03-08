package com.avicodes.halchalin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.CityRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import com.avicodes.halchalin.presentation.ui.home.HomeActivityViewModel

class MainActivityViewModelFactory (
    private val cityRepository: CityRepository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            cityRepository
        ) as T
    }
}