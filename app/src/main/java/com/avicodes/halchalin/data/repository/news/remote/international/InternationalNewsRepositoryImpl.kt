package com.avicodes.halchalin.data.repository.news.remote.international

import android.util.Log
import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSourceImpl.CacheInternationalNewsDataSourceImpl
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class InternationalNewsRepositoryImpl(
    private val cacheInternationalNewsDataSource: CacheInternationalNewsDataSource,
    private val remoteInternationalNewsDataSource: RemoteInternationalNewsDataSource
) : InternationalNewsRepository {

    override fun getNews(
        lang: String,
        topic: String,
        country: String
    ): Flow<PagingData<NewsRemote>> {
        return remoteInternationalNewsDataSource.getNews(
            lang = lang,
            topic = topic,
            country = country
        )
    }
}