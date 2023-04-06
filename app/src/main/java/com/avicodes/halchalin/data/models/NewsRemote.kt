package com.avicodes.halchalin.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "remote_news_table")
data class NewsRemote(
    @PrimaryKey(autoGenerate = false)
    val newsId: Int,
    @SerializedName("content")
    val content: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("image_url")
    val image_url: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("pubDate")
    val pubDate: String?,
    @SerializedName("source_id")
    val source_id: String?,
    @SerializedName("title")
    val title: String?,
): Parcelable