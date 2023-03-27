package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var newsId: String? = null,
    var newsHeadline: String? = null,
    var newsDesc: String? = null,
    var location: String? = null,
    var videoUrl: String? = null,
    var coverUrl: String? = null,
    var resUrls: List<String>? = null,
    var createdAt: Long = 0,
): Parcelable