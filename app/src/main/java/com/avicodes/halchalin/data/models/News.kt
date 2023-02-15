package com.avicodes.halchalin.data.models

data class News(
    var newsId: String? = null,
    var newsHeadline: String? = null,
    var newsDesc: String? = null,
    var location: String? = null,
    var time: String? = null,
    var date: String? = null,
    var likes : ArrayList<String> = ArrayList(),
    var outputUrl: String? = null,
    var type: String? = null,
)