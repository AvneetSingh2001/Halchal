package com.avicodes.halchalin.data.repository.news.remote.international.dataSource

import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteInternationalNewsDataSource {
    fun getNews(
        topic: String,
        country: String,
        lang: String
    ): Flow<PagingData<NewsRemote>>
}