package com.avicodes.halchalin.data.repository.news.local

import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.repository.news.local.dataSource.CacheLocalNewsDataSource
import com.avicodes.halchalin.data.repository.news.local.dataSource.RemoteLocalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.LocalNewsRepository
import kotlinx.coroutines.flow.*

class LocalNewsRepositoryImpl(
    private val cacheLocalNewsDataSource: CacheLocalNewsDataSource,
    private val remoteLocalNewsDataSource: RemoteLocalNewsDataSource,
) : LocalNewsRepository {

    private var _news: MutableStateFlow<Result<List<News>>> =
        MutableStateFlow(Result.NotInitialized)

    override val news: MutableStateFlow<Result<List<News>>>
        get() = _news

    override suspend fun getNews(location: String) {
        _news.value = Result.Loading("Fetching")
        getNewsFromCache(location)
    }

    override suspend fun updateNews(location: String) {
        getNewsFromRemote(location).collectLatest {
            _news.value = it
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

    suspend fun getNewsFromCache(location: String) {
        var newsList = cacheLocalNewsDataSource.getNewsFromCache()

        if (newsList.size > 0) {
            _news.value = Result.Success(newsList)
        } else {
            getNewsFromRemote(location).collectLatest {
                _news.value = it
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

    override fun postComment(itemId: String, comment: String, userId: String): Flow<Result<String>> {
        return remoteLocalNewsDataSource.postComment(
            itemId = itemId,
            comment = comment,
            userId = userId
        )
    }

    override fun getComment(itemId: String): Flow<Result<List<Comment>>> {
        return remoteLocalNewsDataSource.getAllComments(itemId)
    }

    override suspend fun createDynamicLink(news: News): Flow<Result<String>> {
        return remoteLocalNewsDataSource.createDynamicLink(news)
    }

    override fun getNewsById(newsId: String): Flow<Result<News>> {
        return remoteLocalNewsDataSource.getNewsById(newsId)
    }

    override fun deleteComment(commentId: String): Flow<Result<String>> {
        return remoteLocalNewsDataSource.deleteComment(commentId)
    }


}
