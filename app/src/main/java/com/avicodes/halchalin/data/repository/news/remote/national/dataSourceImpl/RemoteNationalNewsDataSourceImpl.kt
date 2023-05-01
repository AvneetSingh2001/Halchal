package com.avicodes.halchalin.data.repository.news.remote.national.dataSourceImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.remote.RemoteNewsPagingSource
import com.avicodes.halchalin.data.repository.news.remote.national.dataSource.RemoteNationalNewsDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class RemoteNationalNewsDataSourceImpl(
    private val newsApiService: NewsApiService,
): RemoteNationalNewsDataSource {

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