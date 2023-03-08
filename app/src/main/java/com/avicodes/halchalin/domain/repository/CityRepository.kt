package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.City
import kotlinx.coroutines.flow.Flow
import com.avicodes.halchalin.data.utils.Result

interface CityRepository {
    fun getAllCities(): Flow<Result<List<City>>>
}