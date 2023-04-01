package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteNewsDataSource {

    suspend fun getNationalHeadlines(
        country: String,
        lang: String
    ): Response<NewsResponse>

    suspend fun getTopicHeadlines(
        topic: String,
        country: String,
        lang: String
    ): Response<NewsResponse>

    fun createRemoteNewsDynamicLink(newsRemote: NewsRemote): Flow<Result<String>>

    fun getCategories() : Flow<Result<List<Categories>>>

}