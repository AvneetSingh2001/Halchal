package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface AdsRepository {
    fun getAllFeaturedAds(): Flow<Result<List<Featured>>>
}