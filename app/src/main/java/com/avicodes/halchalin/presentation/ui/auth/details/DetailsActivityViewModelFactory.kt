package com.avicodes.halchalin.presentation.ui.auth.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.avicodes.halchalin.domain.repository.CityRepository
import com.avicodes.halchalin.domain.repository.UserRespository

class DetailsActivityViewModelFactory(
    private val userRespository: UserRespository,
    private val cityRepository: CityRepository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsActivityViewModel(
            userRespository = userRespository,
            cityRepository = cityRepository
            ) as T
    }
}