package com.avicodes.halchalin.data.repository.news.national.dataSourceImpl

import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.national.dataSource.CacheNationalNewsDataSource

class CacheNationalNewsDataSourceImpl(
) : CacheNationalNewsDataSource {
    private var newsList: NewsResponse? = null
    override suspend fun getNewsFromCache(): NewsResponse? {
        return newsList
    }

    override suspend fun saveNewsInCache(news: NewsResponse) {
        var oldList = ArrayList<NewsRemote>()

        if(news.nextPage != "2") {
            newsList?.let {
                oldList = ArrayList(it.results)
            }
        }

        val res = NewsResponse(
            nextPage = news.nextPage,
            results = oldList + news.results,
            status = news.status,
            totalResults = news.totalResults
        )

        newsList = res
    }
}