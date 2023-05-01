package com.avicodes.halchalin.data.repository.news.remote.dataSource

import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface RemoteNewsDataSource {
    fun getNews(
        topic: String,
        country: String,
        lang: String
    ): Flow<PagingData<NewsRemote>>

    fun getCategories() : Flow<Result<List<Categories>>>
}