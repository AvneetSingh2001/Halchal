package com.avicodes.halchalin.data.repository.news.category.dataSourceImpl

import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.category.dataSource.RemoteCategoryNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import retrofit2.Response

class RemoteCategoryNewsDataSourceImpl(
    private val newsApiService: NewsApiService,
    private val firestore: FirebaseFirestore
): RemoteCategoryNewsDataSource {

    override suspend fun getNews(
        page: String?,
        topic: String,
        country: String,
        lang: String
    ): Response<NewsResponse> {
        return newsApiService.getTopicHeadlines(
            topic = topic,
            country = country,
            lang = lang,
            page = page,
        )
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