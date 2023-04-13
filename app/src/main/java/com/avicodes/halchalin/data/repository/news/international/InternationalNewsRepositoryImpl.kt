package com.avicodes.halchalin.data.repository.news.international

import android.util.Log
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSourceImpl.CacheInternationalNewsDataSourceImpl
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InternationalNewsRepositoryImpl(
    private val cacheInternationalNewsDataSource: CacheInternationalNewsDataSource,
    private val remoteInternationalNewsDataSource: RemoteInternationalNewsDataSource
) : InternationalNewsRepository {

    override suspend fun getNews() = flow<Result<NewsResponse>> {
        emit(Result.Loading("Fetching"))
        try {
            var newsRes = getNewsFromCache()
            if (newsRes != null) {
                emit(Result.Success(newsRes))
            } else {
                newsRes = getNewsFromRemote(1)
                emit(Result.Success(newsRes))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun updateNews(page: Int) = flow<Result<NewsResponse>> {
        emit(Result.Loading("Fetching"))
        try {
            val newsRes = getNewsFromRemote(page)
            if (newsRes != null) {
                cacheInternationalNewsDataSource.saveNewsInCache(newsRes)
                emit(Result.Success(newsRes))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun getNewsFromRemote(page: Int): NewsResponse? {
        var newsRes: NewsResponse? = null
        try {
            val response = remoteInternationalNewsDataSource.getNews(page)
            val body = response.body()
            if (body != null) {
                newsRes = body
            }
        } catch (exception: Exception) {
            Log.i("MyTag", exception.message.toString())
        }
        return newsRes
    }

    suspend fun getNewsFromCache(): NewsResponse? {
        var newsList: NewsResponse? = null

        try {
            newsList = cacheInternationalNewsDataSource.getNewsFromCache()
        } catch (exception: Exception) {
        }

        newsList?.let {
            if (it.results.isNotEmpty()) {
                return newsList
            }
        }

        newsList = getNewsFromRemote(page = 1)
        newsList?.let {
            cacheInternationalNewsDataSource.saveNewsInCache(it)
        }

        return newsList
    }


}