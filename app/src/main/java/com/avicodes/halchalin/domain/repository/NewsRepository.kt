package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsResponse
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
}