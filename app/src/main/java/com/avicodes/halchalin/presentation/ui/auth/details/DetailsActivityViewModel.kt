package com.avicodes.halchalin.presentation.ui.auth.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.domain.repository.CityRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailsActivityViewModel(
    private val userRespository: UserRespository,
    private val cityRepository: CityRepository
): ViewModel() {

    fun saveUser(
        name: String,
        loc: String,
    ) {
        val user = User(
            name = name,
            location = loc,
        )
        userRespository.saveUser(user)
    }

    fun login() = viewModelScope.launch(Dispatchers.IO){
        userRespository.login()
    }

    fun getCities() = liveData {
        cityRepository.getAllCities().collectLatest {
            emit(it)
        }
    }

}