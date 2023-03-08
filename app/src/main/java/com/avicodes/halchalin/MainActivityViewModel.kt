package com.avicodes.halchalin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.avicodes.halchalin.domain.repository.CityRepository
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val cityRepository: CityRepository
): ViewModel() {


}