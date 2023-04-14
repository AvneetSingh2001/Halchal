package com.avicodes.halchalin.data.repository.news.national.dataSourceImpl

import android.util.Log
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.national.dataSource.CacheNationalNewsDataSource

class CacheNationalNewsDataSourceImpl(
) : CacheNationalNewsDataSource {
    private var newsList: NewsResponse? = null
    override suspend fun getNewsFromCache(): NewsResponse? {
        Log.e("App Flow", "GetInterNationalNewsFromCache " + newsList?.results.toString())
        return newsList
    }

    override suspend fun saveNewsInCache(news: NewsResponse) {
        var oldList = ArrayList<NewsRemote>()

        newsList?.let {
            if (news.nextPage != it.nextPage) {
                oldList = ArrayList(it.results)
            }
        }

        val res = NewsResponse(
            nextPage = news.nextPage,
            results = oldList + news.results,
            status = news.status,
            totalResults = news.totalResults
        )

        Log.e("App Flow", "SaveInterNationalNewsFromCache " + res.results.toString())
        newsList = res
    }
}