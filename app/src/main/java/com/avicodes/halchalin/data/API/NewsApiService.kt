package com.avicodes.halchalin.data.API

import com.avicodes.halchalin.BuildConfig
import com.avicodes.halchalin.data.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("news")
    suspend fun getTopHeadlines(
        @Query("country")
        country: String,
        @Query("language")
        lang: String,
        @Query("apikey")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<NewsResponse>

    @GET("news")
    suspend fun getTopicHeadlines(
        @Query("category")
        topic: String,
        @Query("country")
        country: String,
        @Query("language")
        lang: String,
        @Query("apikey")
        apiKey: String = BuildConfig.API_KEY,
        @Query("page")
        page: Int = 1
    ): Response<NewsResponse>
}