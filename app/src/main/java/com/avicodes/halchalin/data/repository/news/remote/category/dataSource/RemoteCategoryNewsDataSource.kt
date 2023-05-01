package com.avicodes.halchalin.data.repository.news.remote.category.dataSource

import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteCategoryNewsDataSource {
    fun getNews(
        topic: String,
        country: String,
        lang: String
    ): Flow<PagingData<NewsRemote>>

    fun getCategories() : Flow<Result<List<Categories>>>
}