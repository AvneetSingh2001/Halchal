package com.avicodes.halchalin.presentation.ui.auth.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.domain.repository.CityRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DetailsFragmentViewModel(
    private val userRespository: UserRespository,
    private val cityRepository: CityRepository
): ViewModel() {

    fun saveUser(
        name: String,
        loc: String,
        phone: String,
    ) {
        val user = User(
            name = name,
            mobile = "$phone",
            location = loc,
            userId = phone
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