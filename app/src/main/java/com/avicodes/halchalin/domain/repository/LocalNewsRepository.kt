package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalNewsRepository {

    fun getNews(location: String): Flow<Result<List<News>>>

    fun postComment(
        newsId: String,
        comment: String,
        userId: String,
    ): Flow<Result<String>>

    fun getComment(
        newsId: String
    ): Flow<Result<List<Comment>>>

    suspend fun createDynamicLink(
        news: News
    ): Flow<Result<String>>

    fun getNewsById(newsId: String): Flow<Result<News>>

    fun deleteComment(
        commentId: String,
    ): Flow<Result<String>>
}