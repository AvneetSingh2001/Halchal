package com.avicodes.halchalin.data.repository.news.local.dataSource

import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteLocalNewsDataSource {
    fun getNews(location: String): Flow<Result<List<News>>>

    fun getNewsById(id: String): Flow<Result<News>>
    fun getAllComments(itemId: String): Flow<Result<List<Comment>>>

    fun postComment(
        itemId: String,
        comment: String,
        userId: String,
    ): Flow<Result<String>>

    suspend fun createDynamicLink(
        news: News
    ) : Flow<Result<String>>

    fun deleteComment(
        commentId: String,
    ): Flow<Result<String>>

}