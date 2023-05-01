package com.avicodes.halchalin.data.repository.news.remote

import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.repository.news.remote.dataSource.RemoteNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.RemoteNewsRepository
import kotlinx.coroutines.flow.Flow

class RemoteNewsRepositoryImpl(
    private val remoteNewsDataSource: RemoteNewsDataSource
): RemoteNewsRepository {
    override fun getNews(
        lang: String,
        topic: String,
        country: String
    ): Flow<PagingData<NewsRemote>> {
        return remoteNewsDataSource.getNews(
            lang = lang,
            topic = topic,
            country = country
        )
    }

    override fun getCategories(): Flow<Result<List<Categories>>> {
        return remoteNewsDataSource.getCategories()
    }
}