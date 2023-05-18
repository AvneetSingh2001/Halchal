package com.avicodes.halchalin.data.repository.news.local.dataSourceImpl

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.avicodes.halchalin.data.repository.news.local.dataSource.RemoteLocalNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class RemoteLocalNewsDataSourceImpl(
    val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userPrefs: UserPrefs
) : RemoteLocalNewsDataSource {

    override fun getNews(location: String) = flow<Result<List<News>>> {
        emit(Result.Loading("Fetching News"))
        var arr = location.split(", ").toTypedArray()
        var district = ""
        if (arr.size == 3) {
            district = arr[1] + ", " + arr[2]
        }
        val snapshot = firestore
            .collection("News")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .whereEqualTo("district", district)
            .get().await()
        val news = snapshot.toObjects(News::class.java)
        emit(Result.Success(news))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)


    override fun getAllComments(newsId: String) = flow<Result<List<Comment>>> {
        emit(Result.Loading("Fetching Comments"))
        val snapshot = firestore
            .collection("Comments")
            .document(newsId)
            .collection(newsId)
            .orderBy("time", Query.Direction.DESCENDING)
            .get().await()
        val comment = snapshot.toObjects(Comment::class.java)
        emit(Result.Success(comment))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun getNewsById(id: String) = flow<Result<News>> {
        emit(Result.Loading("Fetching News"))
        val snapshot = firestore
            .collection("News")
            .document(id)
            .get()
            .await()

        val news = snapshot.toObject(News::class.java)
        emit(Result.Success(news))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun postComment(
        newsId: String,
        comment: String,
    ) = flow<Result<String>> {
        emit(Result.Loading("Uploading Comment"))
        userPrefs.getUserId().collectLatest {
            val userComment = Comment(
                System.currentTimeMillis(),
                comment,
                it
            )
            firestore
                .collection("Comments")
                .document(newsId)
                .collection(newsId)
                .document().set(userComment)
                .await()
        }
        emit(Result.Success("Uploaded"))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override suspend fun createDynamicLink(news: News): Flow<Result<String>> = flow {
        emit(Result.Loading("Creating Dynamic Link"))
        try {
            val dynamicLink = Firebase.dynamicLinks.shortLinkAsync {
                link = Uri.parse("https://www.halchalin.com/?news=${news.newsId}")
                domainUriPrefix = "https://halchalin.page.link"
                // Open links with this app on Android
                androidParameters { }
                // Open links with com.example.ios on iOS
                iosParameters("www.kichhakihalchal.com") {
                }

                socialMetaTagParameters {
                    title = "Halchal News"
                    description = "हर हलचल पर हमारी नज़र"
                    imageUrl = news.coverUrl?.toUri() ?: news.coverUrl?.toUri()
                            ?: "https://firebasestorage.googleapis.com/v0/b/halchal-bb06e.appspot.com/o/logo%2Fhalchal.png?alt=media&token=0fab102e-d7d2-4fa2-95c4-b2665f37dc05".toUri()
                }
            }.await()

            dynamicLink.shortLink?.let {
                emit(Result.Success(news.newsHeadline + "\n\n" + it.toString()))
            }

        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}