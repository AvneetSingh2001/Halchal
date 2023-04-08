package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.db.remoteNews.InternationalNewsDao
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.repository.dataSource.CacheInternationalNewsDataSource

class CacheInternationalNewsDataSourceImpl(
    private val internationalNewsDao: InternationalNewsDao
): CacheInternationalNewsDataSource {
    override suspend fun insertNews(news: NewsRemote) {
        internationalNewsDao.insert(news)
    }

    override suspend fun getAllInternationalNews(): List<NewsRemote> {
        return internationalNewsDao.getAllArticles()
    }

    override suspend fun deleteAllInternationalNews() {
        return internationalNewsDao.deleteAll()
    }
}