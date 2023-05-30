package com.avicodes.halchalin.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Featured(
    var adId: String = "",
    var imgUrl: String = "",
    var priority: Int = 0,
    var loc: String? = null
)
