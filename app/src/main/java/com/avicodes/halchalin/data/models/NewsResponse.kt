package com.avicodes.halchalin.data.models

data class NewsResponse(
    val nextPage: String,
    val results: List<NewsRemote>,
    val status: String,
    val totalResults: Int
)