package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface InternationalNewsRepository {
    suspend fun getNews(): Flow<Result<NewsResponse>>
    suspend fun updateNews(page: Int): Flow<Result<NewsResponse>>
}