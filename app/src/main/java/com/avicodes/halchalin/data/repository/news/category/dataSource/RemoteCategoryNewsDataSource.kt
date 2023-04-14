package com.avicodes.halchalin.data.repository.news.category.dataSource

import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteCategoryNewsDataSource {
    suspend fun getNews(
        page: String?,
        topic: String,
        country: String,
        lang: String
    ): Response<NewsResponse>

    fun getCategories() : Flow<Result<List<Categories>>>
}