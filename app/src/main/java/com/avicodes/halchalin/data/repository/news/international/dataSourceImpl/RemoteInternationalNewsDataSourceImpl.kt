package com.avicodes.halchalin.data.repository.news.international.dataSourceImpl

import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.international.dataSource.RemoteInternationalNewsDataSource
import retrofit2.Response

class RemoteInternationalNewsDataSourceImpl(
    private val newsApiService: NewsApiService,
): RemoteInternationalNewsDataSource {
    override suspend fun getNews(
        page: String?
    ): Response<NewsResponse> {
        return newsApiService.getTopicHeadlines(
            topic = "world",
            country = "in",
            lang = "hi",
            page = page
        )
    }
}