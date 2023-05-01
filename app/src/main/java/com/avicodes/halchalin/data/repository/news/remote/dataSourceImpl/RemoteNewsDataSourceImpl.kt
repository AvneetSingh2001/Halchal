package com.avicodes.halchalin.data.repository.news.remote.dataSourceImpl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.repository.news.remote.RemoteNewsPagingSource
import com.avicodes.halchalin.data.repository.news.remote.dataSource.RemoteNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class RemoteNewsDataSourceImpl(
    private val newsApiService: NewsApiService,
    private val firestore: FirebaseFirestore
): RemoteNewsDataSource {

    override fun getNews(
        topic: String,
        country: String,
        lang: String
    ): Flow<PagingData<NewsRemote>> {
        Log.e("Avneet Data getNews","at getnews")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RemoteNewsPagingSource(
                    service = newsApiService,
                    topic = topic,
                    country = country,
                    lang = lang,
                    category = topic
                )
            }
        ).flow
    }

    override fun getCategories() = flow<Result<List<Categories>>> {
        emit(Result.Loading("Fetching Category"))

        val snapshot = firestore
            .collection("Categories")
            .get().await()
        val category = snapshot.toObjects(Categories::class.java)
        emit(Result.Success(category))

    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}