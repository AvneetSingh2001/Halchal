package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.internal.ChannelFlow

interface LocalNewsDataSource {
    fun getAllLocalNews(location: String): Flow<Result<List<News>>>
    suspend fun getNearbyNews(location: String): Flow<Result<List<News>>>
    fun getNewsById(id: String): Flow<Result<News>>
    fun getAllComments(newsId: String): Flow<Result<List<Comment>>>

    fun postComment(
        newsId: String,
        comment: String,
    ): Flow<Result<String>>

    suspend fun createDynamicLink(
        news: News
    ) : Flow<Result<String>>
}