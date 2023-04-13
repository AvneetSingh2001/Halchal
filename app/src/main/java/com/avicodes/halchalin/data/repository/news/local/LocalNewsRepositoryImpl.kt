package com.avicodes.halchalin.data.repository.news.local

import android.location.Location
import android.util.Log
import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSourceImpl.CacheInternationalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.local.dataSource.CacheLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.dataSource.RemoteLocalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import com.avicodes.halchalin.domain.repository.LocalNewsRepository
import kotlinx.coroutines.flow.*

class LocalNewsRepositoryImpl(
    private val cacheLocalNewsDataSource: CacheLocalNewsDataSource,
    private val remoteLocalNewsDataSource: RemoteLocalNewsDataSource
) : LocalNewsRepository {

    override suspend fun getNews(location: String): Flow<Result<List<News>>> {
        return getNewsFromCache(location)
    }

    override suspend fun updateNews(location: String) = flow<Result<List<News>>> {
        getNewsFromRemote(location).collectLatest {
            emit(it)
            when (it) {
                is Result.Success -> {
                    it.data?.let { news ->
                        cacheLocalNewsDataSource.saveNewsInCache(news)
                    }
                }
                else -> {}
            }
        }
    }

    fun getNewsFromRemote(location: String): Flow<Result<List<News>>> {
        return remoteLocalNewsDataSource.getNews(location)
    }

    suspend fun getNewsFromCache(location: String) = flow<Result<List<News>>> {
        var newsList: List<News>? = null

        try {
            newsList = cacheLocalNewsDataSource.getNewsFromCache()
        } catch (exception: Exception) {
        }

        if (newsList != null) {
            emit(Result.Success(newsList))
        } else {
            getNewsFromRemote(location).collectLatest {
                emit(it)
                when (it) {
                    is Result.Success -> {
                        it.data?.let { news ->
                            cacheLocalNewsDataSource.saveNewsInCache(news)
                        }
                    }
                    else -> {}
                }
            }

        }
    }

    override fun postComment(newsId: String, comment: String): Flow<Result<String>> {
        return remoteLocalNewsDataSource.postComment(
            newsId = newsId,
            comment = comment,
        )
    }

    override fun getComment(newsId: String): Flow<Result<List<Comment>>> {
        return remoteLocalNewsDataSource.getAllComments(newsId)
    }

    override suspend fun createDynamicLink(news: News): Flow<Result<String>> {
        return remoteLocalNewsDataSource.createDynamicLink(news)
    }

    override fun getNewsById(newsId: String): Flow<Result<News>> {
        return remoteLocalNewsDataSource.getNewsById(newsId)
    }
}