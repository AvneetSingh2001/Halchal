package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import retrofit2.Response

interface NewsRepository {
    suspend fun getNationalHeadlines(
        country: String,
        lang: String
    ): Result<NewsResponse>
}