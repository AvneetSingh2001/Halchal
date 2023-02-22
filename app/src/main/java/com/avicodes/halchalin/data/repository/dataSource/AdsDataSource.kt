package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.FeaturedAds
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface AdsDataSource {
    fun getAllFeaturedAds(): Flow<Result<List<FeaturedAds>>>
}