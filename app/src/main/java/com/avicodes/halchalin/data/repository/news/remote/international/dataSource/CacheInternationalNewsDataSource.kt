package com.avicodes.halchalin.data.repository.news.remote.international.dataSource

import com.avicodes.halchalin.data.models.NewsLocal
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse

interface CacheInternationalNewsDataSource {
    suspend fun getNewsFromCache() : NewsResponse?
    suspend fun saveNewsInCache(news: NewsResponse)
}