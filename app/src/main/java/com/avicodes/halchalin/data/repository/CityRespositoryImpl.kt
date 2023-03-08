package com.avicodes.halchalin.data.repository

import com.avicodes.halchalin.data.models.City
import com.avicodes.halchalin.data.repository.dataSource.CityDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow

class CityRespositoryImpl(
    val cityDataSource: CityDataSource
): CityRepository {
    override fun getAllCities(): Flow<Result<List<City>>> {
        return cityDataSource.getAllLocations()
    }
}