package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalNewsDataSource {
    fun getAllLocalNews(location: String): Flow<List<News>>
    suspend fun getNearbyNews(location: String): Flow<List<News>>
    suspend fun getNewsById(id: String): Result<News>
}