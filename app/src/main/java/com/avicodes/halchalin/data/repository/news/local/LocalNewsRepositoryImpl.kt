package com.avicodes.halchalin.data.repository.news.local

import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.LocalNewsRepository
import kotlinx.coroutines.flow.*

class LocalNewsRepositoryImpl(
    private val remoteLocalNewsDataSource: RemoteLocalNewsDataSource,
) : LocalNewsRepository {


    override fun getNews(location: String): Flow<Result<List<News>>> {
        return getNewsFromRemote(location)
    }


    fun getNewsFromRemote(location: String): Flow<Result<List<News>>> {
        return remoteLocalNewsDataSource.getNews(location)
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
