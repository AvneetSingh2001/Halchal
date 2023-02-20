package com.avicodes.halchalin.data.API

import com.avicodes.halchalin.BuildConfig
import com.avicodes.halchalin.data.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        country: String,
        @Query("lang")
        lang: String,
        @Header("X-RapidAPI-Key")
        apiKey: String = BuildConfig.API_KEY
    ): Response<NewsResponse>

    @GET("topic-headlines")
    suspend fun getTopicHeadlines(
        @Query("topic")
        topic: String,
        @Query("country")
        country: String,
        @Query("lang")
        lang: String,
        @Header("X-RapidAPI-Key")
        apiKey: String = BuildConfig.API_KEY
    ): Response<NewsResponse>
}