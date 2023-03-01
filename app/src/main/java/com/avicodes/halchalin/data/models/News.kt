package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var newsId: String? = null,
    var newsHeadline: String? = null,
    var newsDesc: String? = null,
    var location: String? = null,
    var time: String? = null,
    var date: String? = null,
    var likes : List<String> = ArrayList(),
    var comments: List<Pair<User, String>> = ArrayList(),
    var videoUrl: String? = null,
    var imageUrl: String? = null,
    var coverUrl: String? = null,
    var type: String? = null,
    var resUrls: List<String>? = null,
    var createdAt: Long = 0,
): Parcelable