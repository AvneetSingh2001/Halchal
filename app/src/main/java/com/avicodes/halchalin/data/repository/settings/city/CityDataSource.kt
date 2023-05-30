package com.avicodes.halchalin.data.repository.settings.city

import com.avicodes.halchalin.data.models.City
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import com.avicodes.halchalin.data.utils.Result

interface CityDataSource{
    fun getAllLocations(): Flow<Result<List<City>>>

}