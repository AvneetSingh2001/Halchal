package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.NewsRemote

interface CacheNationalNewsDataSource {
    suspend fun insertNews(news: NewsRemote)
    suspend fun getAllNationalNews(): List<NewsRemote>
    suspend fun deleteAllNationalNews()
}