package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalNewsRepository {
    suspend fun getNews(location: String): Flow<Result<List<News>>>

    suspend fun updateNews(location: String): Flow<Result<List<News>>>

    fun postComment(
        newsId: String,
        comment: String,
    ): Flow<Result<String>>

    fun getComment(
        newsId: String
    ): Flow<Result<List<Comment>>>

    suspend fun createDynamicLink(
        news: News
    ): Flow<Result<String>>

    fun getNewsById(newsId: String): Flow<Result<News>>
}