package com.avicodes.halchalin.data.repository.ads.featured

import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface AdsDataSource {
    fun getAllFeaturedAds(): Flow<Result<List<Featured>>>
}