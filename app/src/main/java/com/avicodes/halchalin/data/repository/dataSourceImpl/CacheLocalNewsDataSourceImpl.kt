package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.db.localNews.LocalNewsDao
import com.avicodes.halchalin.data.models.NewsLocal
import com.avicodes.halchalin.data.repository.dataSource.CacheLocalNewsDataSource
import kotlinx.coroutines.flow.Flow

class CacheLocalNewsDataSourceImpl(
    private var localNewsDao: LocalNewsDao
): CacheLocalNewsDataSource {
    override suspend fun insertNews(news: NewsLocal) {
        localNewsDao.insert(news)
    }

    override suspend fun getAllLocalNews(): List<NewsLocal> {
        return localNewsDao.getAllArticles()
    }

    override suspend fun deleteAllLocalNews() {
        localNewsDao.deleteAll()
    }
}