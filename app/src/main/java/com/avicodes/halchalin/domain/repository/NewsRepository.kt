package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.*
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow


interface NewsRepository {
    suspend fun getNationalHeadlines(
        country: String,
        lang: String
    ): Result<NewsResponse>

    suspend fun getTopicHeadlines(
        topic: String,
        country: String,
        lang: String
    ): Result<NewsResponse>

    fun getLocalNews(
        location: String
    ): Flow<Result<List<News>>>

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

    fun createRemoteNewsDynamicLink(newsRemote: NewsRemote): Flow<Result<String>>
}