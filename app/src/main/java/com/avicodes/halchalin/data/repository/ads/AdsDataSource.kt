package com.avicodes.halchalin.data.repository.ads

import android.net.Uri
import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.TopAds
import com.avicodes.halchalin.data.models.ads
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface AdsDataSource {
    fun getAllFeaturedAds(): Flow<Result<List<Featured>>>
    fun getAllNormalAds(): Flow<Result<List<ads>>>

    fun getAllTopAds() : Flow<Result<List<TopAds>>>
}