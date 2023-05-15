package com.avicodes.halchalin.data.repository.ads

import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.ads
import com.avicodes.halchalin.data.repository.ads.featured.AdsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.AdsRepository
import kotlinx.coroutines.flow.Flow

class AdsRepositoryImpl(
    private val adsDataSource: AdsDataSource
): AdsRepository {
    override fun getAllFeaturedAds(): Flow<Result<List<Featured>>> {
        return adsDataSource.getAllFeaturedAds()
    }

    override fun getAllNormalAds(): Flow<Result<List<ads>>> {
        return adsDataSource.getAllNormalAds()
    }
}