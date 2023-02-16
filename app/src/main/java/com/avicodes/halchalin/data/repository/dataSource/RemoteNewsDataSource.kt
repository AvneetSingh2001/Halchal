package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.NewsResponse
import retrofit2.Response

interface RemoteNewsDataSource {

    suspend fun getNationalHeadlines(
        country: String,
        lang: String
    ): Response<NewsResponse>

}