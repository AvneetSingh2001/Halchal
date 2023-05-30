package com.avicodes.halchalin.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Admin(
    val name: String = "",
    val password: String = "",
    val loc : String? = "",
): Parcelable
