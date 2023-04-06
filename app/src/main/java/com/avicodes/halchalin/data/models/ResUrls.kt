package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResUrls(
    val resUrls: List<String>? = null
): Parcelable