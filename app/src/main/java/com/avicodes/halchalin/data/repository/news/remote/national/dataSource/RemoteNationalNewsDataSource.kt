package com.avicodes.halchalin.data.repository.news.remote.national.dataSource

import com.avicodes.halchalin.data.models.NewsResponse
import retrofit2.Response

interface RemoteNationalNewsDataSource {
    suspend fun getNews(page: String?): Response<NewsResponse>
}