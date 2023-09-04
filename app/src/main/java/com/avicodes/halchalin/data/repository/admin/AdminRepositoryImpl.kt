package com.avicodes.halchalin.data.repository.admin

import android.net.Uri
import android.util.Log
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.AdminRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AdminRepositoryImpl(
    private val fireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : AdminRepository {

    override suspend fun checkIsAdmin(): Boolean {
        return if (auth.uid != null) {
            fireStore
                .collection("Admin")
                .document(auth.uid!!)
                .get().await().exists()
        } else {
            false
        }
    }

    override fun uploadNews(
        newsHeadline: String,
        newsDesc: String,
        newsLoc: String,
        videoUri: Uri?,
        uploadedImages: List<Uri>,
        coverUri: Uri?
    ) = flow<Result<String>> {
        emit(Result.Loading("Loading"))
        val newsId = UUID.randomUUID().toString()

        try {
            coroutineScope {
                var videoUrl = async { uploadVideo(videoUri, newsId) }
                var imagesUrl = async { uploadImages(uploadedImages, newsId) }
                var coverUrl = async { uploadCover(coverUri, newsId) }

                val parts = newsLoc.split(",").map { it.trim() }
                val city = parts[0] // "Kichha"
                val district = parts.drop(1).joinToString(", ") // "Udham Singh Nagar, Uttarakhand"

                var news =
                    News(
                        newsId = newsId,
                        newsHeadline = newsHeadline,
                        newsDesc = newsDesc,
                        location = city,
                        district = district,
                        videoUrl = videoUrl.await(),
                        resUrls = imagesUrl.await(),
                        createdAt = System.currentTimeMillis(),
                        coverUrl = coverUrl.await(),
                        reporterId = auth.uid
                    )

                fireStore.collection("News").document(newsId).set(news).await()
                emit(Result.Success("Success"))
            }

        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun uploadImages(uploadedImages: List<Uri>, newsId: String): MutableList<String> {
        var it = 1
        var imagesUrl: MutableList<String> = mutableListOf()
        for (i in uploadedImages) {
            imagesUrl.add(
                storage
                    .getReference("images/${newsId}_$it.")
                    .putFile(i)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
                    .toString()
            )
            it++
        }
        return imagesUrl
    }

    suspend fun uploadVideo(videoUri: Uri?, newsId: String): String? {
        var videoUrl: String? = null
        if (videoUri != null) {
            videoUrl = storage
                .getReference("videos/$newsId")
                .putFile(videoUri)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()
        }
        return videoUrl
    }

    suspend fun uploadCover(coverUri: Uri?, newsId: String): String? {
        var coverUrl: String? = null
        if (coverUri != null) {
            coverUrl = storage
                .getReference("images/${newsId}_cover")
                .putFile(coverUri)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()
        }
        return coverUrl
    }

}
