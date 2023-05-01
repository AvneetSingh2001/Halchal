package com.avicodes.halchalin.data.repository.news.remote.national.dataSourceImpl

import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.remote.national.dataSource.RemoteNationalNewsDataSource
import retrofit2.Response

class RemoteNationalNewsDataSourceImpl(
    private val newsApiService: NewsApiService,
): RemoteNationalNewsDataSource {

    override suspend fun getNews(
        page: String?
    ): Response<NewsResponse> {
        return newsApiService.getTopHeadlines(
            country = "in",
            lang = "hi",
            page = page
        )
    }
}