package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.models.City
import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.repository.dataSource.CityDataSource
import com.google.firebase.firestore.FirebaseFirestore
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class CityDataSourceImpl(
    private val firebaseFirestore: FirebaseFirestore
): CityDataSource {

    override fun getAllLocations(): Flow<Result<List<City>>> = flow {
        emit(Result.Loading("Fetching News"))

        val cityResponse = firebaseFirestore
            .collection("City")
            .orderBy("name",Query.Direction.ASCENDING)
            .get().await()

        val cities = cityResponse.toObjects(City::class.java)
        emit(Result.Success(cities))

    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

}