package com.avicodes.halchalin.data.repository;

import com.avicodes.halchalin.data.models.*
import com.avicodes.halchalin.data.repository.dataSource.LocalNewsDataSource
import com.avicodes.halchalin.data.repository.dataSource.RemoteNewsDataSource;
import com.avicodes.halchalin.data.repository.dataSource.UserDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.NewsRepository
import com.avicodes.halchalin.domain.usecase.authenticationUseCase.GetUserByIdUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class NewsRepositoryImpl(
    private val remoteNewsDataSource: RemoteNewsDataSource,
    private val localNewsDataSource: LocalNewsDataSource,
    private val userDataSource: UserDataSource
) : NewsRepository {
    override suspend fun getNationalHeadlines(country: String, lang: String): Result<NewsResponse> {
        return responseToResource(
            remoteNewsDataSource.getNationalHeadlines(
                country = country,
                lang = lang
            )
        )
    }

    override suspend fun getTopicHeadlines(
        topic: String,
        country: String,
        lang: String
    ): Result<NewsResponse> {
        return responseToResource(
            remoteNewsDataSource.getTopicHeadlines(
                topic = topic,
                country = country,
                lang = lang
            )
        )
    }

    override fun getLocalNews(location: String): Flow<Result<List<News>>> {
        return localNewsDataSource.getAllLocalNews(location)
    }

    override fun postComment(newsId: String, comment: String): Flow<Result<String>> {
        return localNewsDataSource.postComment(
            newsId = newsId,
            comment = comment,
        )
    }

    override fun getComment(newsId: String): Flow<Result<List<Comment>>> {
        return localNewsDataSource.getAllComments(newsId)
    }

    override suspend fun createDynamicLink(news: News): Flow<Result<String>> {
        return localNewsDataSource.createDynamicLink(news)
    }

    override fun getNewsById(newsId: String): Flow<Result<News>> {
        return localNewsDataSource.getNewsById(newsId)
    }

    override fun createRemoteNewsDynamicLink(newsRemote: NewsRemote): Flow<Result<String>> {
        return remoteNewsDataSource.createRemoteNewsDynamicLink(newsRemote)
    }

    override fun getCategories(): Flow<Result<List<Categories>>> {
        return remoteNewsDataSource.getCategories()
    }


    fun responseToResource(response: Response<NewsResponse>): Result<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }
        return Result.Error(Exception(response.message()))
    }
}
