package com.avicodes.halchalin.data.repository.news.remote.national

import android.util.Log
import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSourceImpl.CacheInternationalNewsDataSourceImpl
import com.avicodes.halchalin.data.repository.news.remote.national.dataSource.CacheNationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.national.dataSource.RemoteNationalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import com.avicodes.halchalin.domain.repository.NationalNewsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class NationalNewsRepositoryImpl(
    private val cacheNationalNewsDataSource: CacheNationalNewsDataSource,
    private val remoteNationalNewsDataSource: RemoteNationalNewsDataSource
) : NationalNewsRepository {

    override fun getNews(
        lang: String,
        topic: String,
        country: String
    ): Flow<PagingData<NewsRemote>> {
        return remoteNationalNewsDataSource.getNews(
            lang = lang,
            topic = topic,
            country = country
        )
    }
}