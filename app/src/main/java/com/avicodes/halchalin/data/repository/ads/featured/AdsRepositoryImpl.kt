package com.avicodes.halchalin.data.repository.ads.featured

import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.AdsRepository
import kotlinx.coroutines.flow.Flow

class AdsRepositoryImpl(
    private val adsDataSource: AdsDataSource
): AdsRepository {
    override fun getAllFeaturedAds(): Flow<Result<List<Featured>>> {
        return adsDataSource.getAllFeaturedAds()
    }
}