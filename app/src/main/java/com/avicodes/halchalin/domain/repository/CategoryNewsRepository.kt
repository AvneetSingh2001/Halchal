package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface CategoryNewsRepository {
    suspend fun getNews(
        page: Int,
        lang: String,
        topic: String,
        country: String
    ): Flow<Result<NewsResponse>>

    fun getCategories() : Flow<Result<List<Categories>>>
}
