package com.avicodes.halchalin.data.repository.news.local.dataSource

import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsLocal
import kotlinx.coroutines.flow.Flow

interface CacheLocalNewsDataSource {
    suspend fun getNewsFromCache(): List<News>

    suspend fun saveNewsInCache(news: List<News>)
}