package com.avicodes.halchalin.domain.repository

import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface NationalNewsRepository {
    fun getNews(
        lang: String,
        topic: String,
        country: String
    ): Flow<PagingData<NewsRemote>>
}