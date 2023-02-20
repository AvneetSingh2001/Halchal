package com.avicodes.halchalin.data.repository;

import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.dataSource.RemoteNewsDataSource;
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.NewsRepository
import retrofit2.Response

class NewsRepositoryImpl(
    private val remoteNewsDataSource: RemoteNewsDataSource
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

    fun responseToResource(response: Response<NewsResponse>): Result<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Result.Success(it)
            }
        }

        return Result.Error(Exception(response.message()))
    }
}
