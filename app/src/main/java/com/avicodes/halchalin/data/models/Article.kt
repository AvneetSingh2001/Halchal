package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val articleId: String = "",
    val articleTitle: String = "",
    val articleImage: String = "",
    val articleDesc: String = "",
    val articleTag: String = "",
    val userId: String = "",
    val date: String = "",
    val commentEnabled: Boolean = false
): Parcelable
