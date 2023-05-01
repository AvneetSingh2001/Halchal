package com.avicodes.halchalin.domain.repository

import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface RemoteNewsRepository {
    fun getNews(
        lang: String,
        topic: String,
        country: String
    ): Flow<PagingData<NewsRemote>>

    fun getCategories() : Flow<Result<List<Categories>>>
}
