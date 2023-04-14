package com.avicodes.halchalin.data.repository.news.national

import android.util.Log
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSourceImpl.CacheInternationalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.national.dataSource.CacheNationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.national.dataSource.RemoteNationalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import com.avicodes.halchalin.domain.repository.NationalNewsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class NationalNewsRepositoryImpl(
    private val cacheNationalNewsDataSource: CacheNationalNewsDataSource,
    private val remoteNationalNewsDataSource: RemoteNationalNewsDataSource
) : NationalNewsRepository {

    private var _news: MutableStateFlow<Result<NewsResponse>> =
        MutableStateFlow(Result.NotInitialized)

    override val news: MutableStateFlow<Result<NewsResponse>>
        get() = _news

    override suspend fun getNews() {
        _news.value = Result.Loading("Fetching")
        getNewsFromCache()
    }

    override suspend fun updateNews(page: String?) {
        try {
            getNewsFromRemote(page)
        } catch (e: Exception) {
            _news.value = Result.Error(e)
        }
    }

    suspend fun getNewsFromRemote(page: String?) {
        val response = remoteNationalNewsDataSource.getNews(page)
        if (response.isSuccessful) {
            response.body()?.let {
                cacheNationalNewsDataSource.saveNewsInCache(it)
                Log.e("Avneet", it.results.toString())
            }
            _news.value = Result.Success(response.body())
        } else {
            _news.value = Result.Error(Exception(response.errorBody().toString()))
        }
    }

    suspend fun getNewsFromCache() {
        try {
            var newsList: NewsResponse? = cacheNationalNewsDataSource.getNewsFromCache()

            if (newsList != null) {
                _news.value = Result.Success(newsList)
            } else {
                getNewsFromRemote(null)
            }
        } catch (e: Exception) {
            _news.value = Result.Error(e)
        }
    }

}