package com.avicodes.halchalin.data.repository.news.local.dataSourceImpl

import android.util.Log
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.repository.news.local.dataSource.CacheLocalNewsDataSource

class CacheLocalNewsDataSourceImpl(
): CacheLocalNewsDataSource {

    private var newsList = ArrayList<News>()
    override suspend fun getNewsFromCache(): List<News> {
        Log.e("App Flow", "GetLocalNewsFromCache $newsList")
        return newsList
    }

    override suspend fun saveNewsInCache(news: List<News>) {
        newsList = ArrayList(news)
        Log.e("App Flow", "saveLocalNewsInCache $newsList")
    }

}