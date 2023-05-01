package com.avicodes.halchalin.data.repository.news.remote.international.dataSourceImpl

import android.util.Log
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.CacheInternationalNewsDataSource

class CacheInternationalNewsDataSourceImpl(
) : CacheInternationalNewsDataSource {
    private var newsList: NewsResponse? = null
    override suspend fun getNewsFromCache(): NewsResponse? {
        Log.e("App Flow", "GetNationalNewsFromCache " + newsList?.results.toString())
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
        Log.e("App Flow", "SaveNationalNewsFromCache " + res.results.toString())
        newsList = res
    }
}