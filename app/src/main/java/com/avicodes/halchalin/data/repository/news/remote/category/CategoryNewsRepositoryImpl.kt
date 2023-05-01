package com.avicodes.halchalin.data.repository.news.remote.category

import android.util.Log
import androidx.paging.PagingData
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.remote.category.dataSource.RemoteCategoryNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.remote.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.CategoryNewsRepository
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import com.google.android.datatransport.cct.StringMerger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class CategoryNewsRepositoryImpl(
    private val remoteCategoryNewsDataSource: RemoteCategoryNewsDataSource
) : CategoryNewsRepository {

    private var _news: MutableStateFlow<Result<NewsResponse>> =
        MutableStateFlow(Result.NotInitialized)

    override val news: MutableStateFlow<Result<NewsResponse>>
        get() = _news

    override fun getNews(
        lang: String,
        topic: String,
        country: String
    ): Flow<PagingData<NewsRemote>> {
        return remoteCategoryNewsDataSource.getNews(
            lang = lang,
            topic = topic,
            country = country
        )
    }



    override fun getCategories(): Flow<Result<List<Categories>>> {
        return remoteCategoryNewsDataSource.getCategories()
    }

}