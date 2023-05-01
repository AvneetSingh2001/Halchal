package com.avicodes.halchalin.data.repository.news.remote.international

import android.util.Log
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSourceImpl.CacheInternationalNewsDataSourceImpl
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class InternationalNewsRepositoryImpl(
    private val cacheInternationalNewsDataSource: CacheInternationalNewsDataSource,
    private val remoteInternationalNewsDataSource: RemoteInternationalNewsDataSource
) : InternationalNewsRepository {

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
        val response = remoteInternationalNewsDataSource.getNews(page)
        Log.e("Avneet Singh", response.toString())
        if (response.isSuccessful) {
            response.body()?.let {
                cacheInternationalNewsDataSource.saveNewsInCache(it)
            }
            _news.value = Result.Success(response.body())
        } else {
            _news.value = Result.Error(Exception(response.message().toString()))
        }
    }

    suspend fun getNewsFromCache() {
        try {
            var newsList: NewsResponse? = cacheInternationalNewsDataSource.getNewsFromCache()

            if (newsList != null) {
                _news.value = Result.Success(newsList)
            } else {
                getNewsFromRemote(page = null)
            }
        } catch (e: Exception) {
            _news.value = Result.Error(e)
        }
    }

}