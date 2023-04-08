package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.NewsRemote

interface CacheInternationalNewsDataSource {
    suspend fun insertNews(news: NewsRemote)
    suspend fun getAllInternationalNews(): List<NewsRemote>
    suspend fun deleteAllInternationalNews()
}