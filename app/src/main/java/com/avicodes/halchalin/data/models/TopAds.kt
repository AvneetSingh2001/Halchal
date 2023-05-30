package com.avicodes.halchalin.data.models

data class TopAds(
    var adId: String = "",
    var imgUrl: String = "",
    var priority: Int = 0,
    var loc: String? = null
) {
}