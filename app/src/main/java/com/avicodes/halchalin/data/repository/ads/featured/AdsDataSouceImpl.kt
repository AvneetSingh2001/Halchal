package com.avicodes.halchalin.data.repository.ads.featured

import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.ads
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class AdsDataSouceImpl(
    private var firestore: FirebaseFirestore
) : AdsDataSource {

    override fun getAllFeaturedAds() = flow<Result<List<Featured>>> {
        emit(Result.Loading("Fetching Ads"))
        val snapshot = firestore
            .collection("Featured")
            .orderBy("priority", Query.Direction.DESCENDING)
            .get().await()

        val featuredAds = snapshot.toObjects(Featured::class.java)
        emit(Result.Success(featuredAds))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)


    override fun getAllNormalAds() = flow<Result<List<ads>>> {
        emit(Result.Loading("Fetching Ads"))
        val snapshot = firestore
            .collection("ads")
            .orderBy("priority", Query.Direction.DESCENDING)
            .get().await()

        val featuredAds = snapshot.toObjects(ads::class.java)
        emit(Result.Success(featuredAds))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

}