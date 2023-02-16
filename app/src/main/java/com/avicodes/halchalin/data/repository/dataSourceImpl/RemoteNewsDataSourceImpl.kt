package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.dataSource.RemoteNewsDataSource
import retrofit2.Response

class RemoteNewsDataSourceImpl(
    private val newsApiService: NewsApiService
): RemoteNewsDataSource {

    override suspend fun getNationalHeadlines(
        country: String,
        lang: String
    ): Response<NewsResponse> {
        return newsApiService.getTopHeadlines(
            country = country,
            lang = lang
        )
    }

}