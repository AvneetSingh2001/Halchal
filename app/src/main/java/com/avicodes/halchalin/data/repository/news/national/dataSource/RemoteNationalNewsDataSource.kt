package com.avicodes.halchalin.data.repository.news.national.dataSource

import com.avicodes.halchalin.data.models.NewsResponse
import retrofit2.Response

interface RemoteNationalNewsDataSource {
    suspend fun getNews(page: Int): Response<NewsResponse>
}