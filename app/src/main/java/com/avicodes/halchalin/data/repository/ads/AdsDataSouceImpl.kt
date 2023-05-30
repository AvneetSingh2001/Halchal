package com.avicodes.halchalin.data.repository.ads

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.avicodes.halchalin.data.models.Featured
import com.avicodes.halchalin.data.models.TopAds
import com.avicodes.halchalin.data.models.ads
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.UUID

class AdsDataSouceImpl(
    private var firestore: FirebaseFirestore,
    private var storage: FirebaseStorage
) : AdsDataSource {

    override fun getAllFeaturedAds() = flow<Result<List<Featured>>> {
        emit(Result.Loading("Fetching Ads"))
        val snapshot = firestore
            .collection("FeaturedAds")
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

    override fun getAllTopAds() = flow<Result<List<TopAds>>> {
        emit(Result.Loading("Fetching Ads"))
        val snapshot = firestore
            .collection("TopAds")
            .orderBy("priority", Query.Direction.DESCENDING)
            .get().await()

        val featuredAds = snapshot.toObjects(TopAds::class.java)
        emit(Result.Success(featuredAds))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

}