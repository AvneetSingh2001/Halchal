package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.NewsLocal
import kotlinx.coroutines.flow.Flow

interface CacheLocalNewsDataSource {
    suspend fun insertNews(news: NewsLocal)
    suspend fun getAllLocalNews(): List<NewsLocal>
    suspend fun deleteAllLocalNews()
}