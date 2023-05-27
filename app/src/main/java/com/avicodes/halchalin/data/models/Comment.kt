package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val commentId: String = "",
    val time: Long = 0,
    val comment: String = "",
    val userId: String = "",
    val itemId: String = ""
) : Parcelable