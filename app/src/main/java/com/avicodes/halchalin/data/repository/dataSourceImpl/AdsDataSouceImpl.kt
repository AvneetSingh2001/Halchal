package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.models.FeaturedAds
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.repository.dataSource.AdsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AdsDataSouceImpl(
    private var firestore: FirebaseFirestore
): AdsDataSource {

    override fun getAllFeaturedAds() = flow<Result<List<FeaturedAds>>> {
        emit(Result.Loading("Fetching Ads"))
        val snapshot = firestore
            .collection("FeaturedAds")
            .orderBy("id", Query.Direction.ASCENDING)
            .get().await()

        val featuredAds = snapshot.toObjects(FeaturedAds::class.java)
        emit(Result.Success(featuredAds))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

}