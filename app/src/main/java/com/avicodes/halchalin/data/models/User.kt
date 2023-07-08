package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var userId : String = "",
    var name : String = "",
    var location: String = "",
    var imgUrl: String = "",
    var about: String = "",
): Parcelable

