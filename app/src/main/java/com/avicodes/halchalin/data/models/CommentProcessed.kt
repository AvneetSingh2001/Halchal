package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentProcessed(
    val commentId: String = "",
    val time: Long = 0,
    val comment: String = "",
    val user: User = User(),
    val itemId: String
): Parcelable {
}