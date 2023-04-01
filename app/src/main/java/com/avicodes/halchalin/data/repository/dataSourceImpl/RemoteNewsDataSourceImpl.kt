package com.avicodes.halchalin.data.repository.dataSourceImpl

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.avicodes.halchalin.data.API.NewsApiService
import com.avicodes.halchalin.data.models.Categories
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.models.NewsRemote
import com.avicodes.halchalin.data.models.NewsResponse
import com.avicodes.halchalin.data.repository.dataSource.RemoteNewsDataSource
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import retrofit2.Response

class RemoteNewsDataSourceImpl(
    private val newsApiService: NewsApiService,
    private val firestore: FirebaseFirestore,
) : RemoteNewsDataSource {

    override suspend fun getNationalHeadlines(
        country: String,
        lang: String
    ): Response<NewsResponse> {
        return newsApiService.getTopHeadlines(
            country = country,
            lang = lang
        )
    }

    override suspend fun getTopicHeadlines(
        topic: String,
        country: String,
        lang: String
    ): Response<NewsResponse> {
        return newsApiService.getTopicHeadlines(
            topic = topic,
            country = country,
            lang = lang
        )
    }

    override fun createRemoteNewsDynamicLink(news: NewsRemote): Flow<Result<String>> = flow {
        emit(Result.Loading("Creating Dynamic Link"))
        try {
            val dynamicLink = Firebase.dynamicLinks.shortLinkAsync {
                link = Uri.parse("https://www.halchalin.com/?news=${news.link}")
                domainUriPrefix = "https://halchalin.page.link"
                // Open links with this app on Android
                androidParameters { }
                // Open links with com.example.ios on iOS
                iosParameters("www.kichhakihalchal.com") {
                }

                socialMetaTagParameters {
                    title = "Halchal News"
                    description = "हर हलचल पर हमारी नज़र"
                    imageUrl = news.image_url?.toUri()
                            ?: "https://firebasestorage.googleapis.com/v0/b/halchal-bb06e.appspot.com/o/logo%2Fhalchal.png?alt=media&token=0fab102e-d7d2-4fa2-95c4-b2665f37dc05".toUri()
                }
            }.await()

            dynamicLink.shortLink?.let {
                emit(Result.Success(news.title + "\n\n" + it.toString()))
            }

        } catch (e: Exception) {
            emit(Result.Error(e))
        }

    }

    override fun getCategories() = flow<Result<List<Categories>>> {
        emit(Result.Loading("Fetching Category"))

        val snapshot = firestore
            .collection("Categories")
            .get().await()
        val category = snapshot.toObjects(Categories::class.java)
        emit(Result.Success(category))

    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

}