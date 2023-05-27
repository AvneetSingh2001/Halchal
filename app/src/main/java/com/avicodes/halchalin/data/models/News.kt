package com.avicodes.halchalin.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class News(
    var newsId: String = "",
    var newsHeadline: String? = null,
    var newsDesc: String? = null,
    var location: String? = null,
    var district: String? = null,
    var videoUrl: String? = null,
    var coverUrl: String? = null,
    var resUrls: List<String>? = ArrayList(),
    var createdAt: Long = 0,
): Parcelable