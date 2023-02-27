package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface LocalNewsDataSource {
    fun getAllLocalNews(location: String): Flow<Result<List<News>>>
    suspend fun getNearbyNews(location: String): Flow<Result<List<News>>>
    suspend fun getNewsById(id: String): Flow<Result<News>>

    suspend fun updateLikes(uid: String)
}