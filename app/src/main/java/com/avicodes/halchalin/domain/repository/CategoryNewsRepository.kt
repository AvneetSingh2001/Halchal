package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface CategoryNewsRepository {
    val news: MutableStateFlow<Result<NewsResponse>>
    suspend fun getNews(
        page: String?,
        lang: String,
        topic: String,
        country: String
    )

    fun getCategories() : Flow<Result<List<Categories>>>
}
