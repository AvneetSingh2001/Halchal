package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleProcessed(
    val articleId: String = "",
    val articleTitle: String = "",
    val articleImage: String = "",
    val articleDesc: String = "",
    val articleTag: String = "",
    val user: User = User(),
    val date: String = "",
    val commentEnabled: Boolean = false,
    val featured: Boolean = false
): Parcelable
