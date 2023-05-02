package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface InternationalNewsRepository {
    val news: MutableStateFlow<Result<NewsResponse>>
    suspend fun getNews()
    suspend fun updateNews(page: String?)
}