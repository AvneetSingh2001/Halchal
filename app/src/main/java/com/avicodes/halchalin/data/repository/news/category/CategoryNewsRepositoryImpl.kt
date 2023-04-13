package com.avicodes.halchalin.data.repository.news.category

import android.util.Log
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.news.category.dataSource.RemoteCategoryNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.CacheInternationalNewsDataSource
import com.avicodes.halchalin.data.repository.news.international.dataSource.RemoteInternationalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.CategoryNewsRepository
import com.avicodes.halchalin.domain.repository.InternationalNewsRepository
import com.google.android.datatransport.cct.StringMerger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryNewsRepositoryImpl(
    private val remoteCategoryNewsDataSource: RemoteCategoryNewsDataSource
): CategoryNewsRepository {

    override suspend fun getNews(
        page: Int,
        lang: String,
        topic: String,
        country: String
    ) = flow<Result<NewsResponse>> {
        emit(Result.Loading("Fetching"))
        try {
            val newsRes = getNewsFromRemote(
                page = page,
                lang = lang,
                topic = topic,
                country = country
            )
            emit(Result.Success(newsRes))
        }catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getCategories(): Flow<Result<List<Categories>>> {
        return remoteCategoryNewsDataSource.getCategories()
    }

    suspend fun getNewsFromRemote(
        page: Int,
        lang: String,
        topic: String,
        country: String
    ): NewsResponse {
        lateinit var newsRes : NewsResponse
        try {
            val response = remoteCategoryNewsDataSource.getNews(
                page = page,
                lang = lang,
                topic = topic,
                country = country
            )
            val body = response.body()
            if(body!=null){
                newsRes = body
            }
        } catch (exception: Exception) {
            Log.i("MyTag", exception.message.toString())
        }
        return newsRes
    }


}