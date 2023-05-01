package com.avicodes.halchalin.data.repository.news.remote.international.dataSourceImpl

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.remote.RemoteNewsPagingSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.RemoteInternationalNewsDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteInternationalNewsDataSourceImpl(
    private val newsApiService: NewsApiService,
): RemoteInternationalNewsDataSource {
    override fun getNews(
        topic: String,
        country: String,
        lang: String
    ): Flow<PagingData<NewsRemote>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RemoteNewsPagingSource(
                    service = newsApiService,
                    topic = topic,
                    country = country,
                    lang = lang,
                    category = topic
                )
            }
        ).flow
    }
}