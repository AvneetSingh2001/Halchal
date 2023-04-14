package com.avicodes.halchalin.data.repository.news.category

import android.util.Log
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.category.dataSource.RemoteCategoryNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.CategoryNewsRepository
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import com.google.android.datatransport.cct.StringMerger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class CategoryNewsRepositoryImpl(
    private val remoteCategoryNewsDataSource: RemoteCategoryNewsDataSource
) : CategoryNewsRepository {

    private var _news: MutableStateFlow<Result<NewsResponse>> =
        MutableStateFlow(Result.NotInitialized)

    override val news: MutableStateFlow<Result<NewsResponse>>
        get() = _news

    override suspend fun getNews(
        page: String?,
        lang: String,
        topic: String,
        country: String
    ) {

        getNewsFromRemote(
            page = page,
            lang = lang,
            topic = topic,
            country = country
        )
    }

    suspend fun getNewsFromRemote(
        page: String?,
        lang: String,
        topic: String,
        country: String
    ) {
        _news.value = Result.Loading("Fetching")
        try {
            val response = remoteCategoryNewsDataSource.getNews(
                page = page,
                lang = lang,
                topic = topic,
                country = country
            )
            if (response.isSuccessful) {
                _news.value = Result.Success(response.body())
            } else {
                _news.value = Result.Error(Exception(response.errorBody().toString()))
            }
        }catch (e: Exception) {
            _news.value = Result.Error(e)
        }
    }


    override fun getCategories(): Flow<Result<List<Categories>>> {
        return remoteCategoryNewsDataSource.getCategories()
    }

}