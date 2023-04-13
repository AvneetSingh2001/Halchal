package com.avicodes.halchalin.data.repository.news.national.dataSource

import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse

interface CacheNationalNewsDataSource {
    suspend fun getNewsFromCache() : NewsResponse?
    suspend fun saveNewsInCache(news: NewsResponse)
}