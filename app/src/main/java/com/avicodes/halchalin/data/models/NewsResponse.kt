package com.avicodes.halchalin.data.models

data class NewsResponse(
    val data: List<Data>,
    val request_id: String,
    val status: String
)