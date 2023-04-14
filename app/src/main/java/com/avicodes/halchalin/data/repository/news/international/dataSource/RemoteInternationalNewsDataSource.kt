package com.avicodes.halchalin.data.repository.news.international.dataSource

import com.avicodes.halchalin.data.models.NewsResponse
import retrofit2.Response

interface RemoteInternationalNewsDataSource {
    suspend fun getNews(page: String?): Response<NewsResponse>
}