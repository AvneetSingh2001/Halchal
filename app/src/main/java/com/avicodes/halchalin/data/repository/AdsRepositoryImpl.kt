package com.avicodes.halchalin.data.repository

import com.avicodes.halchalin.data.models.FeaturedAds
import com.avicodes.halchalin.data.repository.dataSource.AdsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.AdsRepository
import kotlinx.coroutines.flow.Flow

class AdsRepositoryImpl(
    private val adsDataSource: AdsDataSource
): AdsRepository {
    override fun getAllFeaturedAds(): Flow<Result<List<FeaturedAds>>> {
        return adsDataSource.getAllFeaturedAds()
    }
}