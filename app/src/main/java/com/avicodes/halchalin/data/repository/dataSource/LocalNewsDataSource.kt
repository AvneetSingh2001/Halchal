package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.News
import kotlinx.coroutines.flow.Flow

interface LocalNewsDataSource {
    fun getAllLocalNews(): Flow<List<News>>

}