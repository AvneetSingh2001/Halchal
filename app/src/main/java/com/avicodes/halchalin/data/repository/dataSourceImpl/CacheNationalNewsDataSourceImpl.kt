package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.db.remoteNews.NationalNewsDao
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.repository.dataSource.CacheNationalNewsDataSource

class CacheNationalNewsDataSourceImpl(
    private var nationalNewsDao: NationalNewsDao
) : CacheNationalNewsDataSource {
    override suspend fun insertNews(news: NewsRemote) {
        nationalNewsDao.insert(news)
    }

    override suspend fun getAllNationalNews(): List<NewsRemote> {
        return nationalNewsDao.getAllArticles()
    }

    override suspend fun deleteAllNationalNews() {
        nationalNewsDao.deleteAll()
    }

}