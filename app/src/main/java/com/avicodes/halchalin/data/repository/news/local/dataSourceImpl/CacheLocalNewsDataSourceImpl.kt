package com.avicodes.halchalin.data.repository.news.local.dataSourceImpl

import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.repository.news.local.dataSource.CacheLocalNewsDataSource

class CacheLocalNewsDataSourceImpl(
): CacheLocalNewsDataSource {

    private var newsList = ArrayList<News>()
    override suspend fun getNewsFromCache(): List<News> {
        return newsList
    }

    override suspend fun saveNewsInCache(news: List<News>) {
        newsList.clear()
        newsList = ArrayList(news)
    }

}