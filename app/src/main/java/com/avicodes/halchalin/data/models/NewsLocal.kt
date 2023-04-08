package com.avicodes.halchalin.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "local_news_table")
data class NewsLocal(
    @PrimaryKey(autoGenerate = false)
    var newsId: String,
    @SerializedName("newsHeadline")
    var newsHeadline: String? = null,
    @SerializedName("newsDesc")
    var newsDesc: String? = null,
    @SerializedName("location")
    var location: String? = null,
    @SerializedName("videoUrl")
    var videoUrl: String? = null,
    @SerializedName("coverUrl")
    var coverUrl: String? = null,
    @SerializedName("resUrls")
    var resUrls: ResUrls? = null,
    @SerializedName("createdAt")
    var createdAt: Long = 0,
): Parcelable