package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.repository.dataSource.LocalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class LocalNewsDataSourceImpl(
    private val firestore: FirebaseFirestore,
): LocalNewsDataSource {
    override fun getAllLocalNews(location: String) = flow<Result<List<News>>> {
        emit(Result.Loading("Fetching News"))
        val snapshot = firestore
            .collection("News")
            .whereEqualTo("location", location)
            .orderBy("time", Query.Direction.DESCENDING)
            .get().await()

        val news = snapshot.toObjects(News::class.java)
        emit(Result.Success(news))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override suspend fun getNearbyNews(location: String)= flow<Result<List<News>>> {
        emit(Result.Loading("Implementing"))
    }

    override suspend fun getNewsById(id: String)= flow<Result<News>> {
        emit(Result.Loading("Fetching News"))
            val snapshot = firestore
                .collection("News")
                .document(id)
                .get()
                .await()

            val news = snapshot.toObject(News::class.java)
            emit(Result.Success(news))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

}