package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Categories(
    var name: String = "",
    var imgUrl: String = "",
    var lang: String = "hi"
) : Parcelable
