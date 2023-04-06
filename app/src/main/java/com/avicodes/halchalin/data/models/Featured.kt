package com.avicodes.halchalin.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "featured_table")
data class Featured(
    @SerializedName("imgUrl")
    var imgUrl: String? = null,
    @PrimaryKey(autoGenerate = false)
    var priority: Int = 0,
    @SerializedName("loc")
    var loc: String? = null
)
