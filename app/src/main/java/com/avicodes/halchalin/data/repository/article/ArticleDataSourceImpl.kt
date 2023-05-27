package com.avicodes.halchalin.data.repository.article

import android.net.Uri
import androidx.core.net.toUri
import com.avicodes.halchalin.data.models.Article
import com.avicodes.halchalin.data.models.ArticleProcessed
import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.iosParameters
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ArticleDataSourceImpl(
    private val userPrefs: UserPrefs,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ArticleDataSource {

    override fun uploadArticle(
        title: String,
        desc: String,
        tag: String,
        imgUri: Uri,
        userId: String,
        enableComment: Boolean
    ) =
        flow<Result<String>> {
            emit(Result.Loading("Loading"))
            var date = System.currentTimeMillis().toString()
            val id = UUID.randomUUID().toString()

            val articleUrl = storage
                .getReference("article/${id}")
                .putFile(imgUri)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()

            var article = Article(
                articleImage = articleUrl,
                articleId = id,
                articleDesc = desc,
                articleTag = tag,
                articleTitle = title,
                date = date,
                userId = userId,
                isCommentEnabled = enableComment
            )

            firestore.collection("Articles").document(article.articleId)
                .set(article).await()

            emit(Result.Success("Uploaded"))
        }.catch {
            emit(Result.Error(it))
        }.flowOn(Dispatchers.IO)

    override fun getAllArticles() = flow<Result<List<Article>>> {
        emit(Result.Loading("Fetching Comments"))
        val snapshot = firestore
            .collection("Articles")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
        val article = snapshot.toObjects(Article::class.java)
        emit(Result.Success(article))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun getFeaturedArticles() = flow<Result<List<Article>>> {
        emit(Result.Loading("Fetching Comments"))
        val snapshot = firestore
            .collection("FeaturedArticles")
            .orderBy("time", Query.Direction.DESCENDING)
            .get()
            .await()
        val article = snapshot.toObjects(Article::class.java)
        emit(Result.Success(article))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)


    override suspend fun createDynamicLink(article: ArticleProcessed): Flow<Result<String>> = flow {
        emit(Result.Loading("Creating Dynamic Link"))
        try {
            val dynamicLink = Firebase.dynamicLinks.shortLinkAsync {
                link = Uri.parse("https://www.halchalin.com/?article=${article.articleId}")
                domainUriPrefix = "https://halchalin.page.link"
                // Open links with this app on Android
                androidParameters { }
                // Open links with com.example.ios on iOS
                iosParameters("www.kichhakihalchal.com") {
                }

                socialMetaTagParameters {
                    title = "Halchal News"
                    description = "हर हलचल पर हमारी नज़र"
                    imageUrl = article.articleImage?.toUri()
                            ?: "https://firebasestorage.googleapis.com/v0/b/halchal-bb06e.appspot.com/o/logo%2Fhalchal.png?alt=media&token=0fab102e-d7d2-4fa2-95c4-b2665f37dc05".toUri()
                }
            }.await()

            dynamicLink.shortLink?.let {
                emit(Result.Success(article.articleTitle+ "\n\n" + it.toString()))
            }

        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getArticleById(articleId: String) = flow<Result<Article>> {
        emit(Result.Loading("Fetching Article"))
        val snapshot = firestore
            .collection("Articles")
            .document(articleId)
            .get()
            .await()

        val article = snapshot.toObject(Article::class.java)
        emit(Result.Success(article))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun getUserArticles(userId: String)= flow<Result<List<Article>>> {
        emit(Result.Loading("Fetching Comments"))
        val snapshot = firestore
            .collection("Articles")
            .orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        val article = snapshot.toObjects(Article::class.java)
        emit(Result.Success(article))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)

    override fun deleteArticle(articleId: String)= flow<Result<String>> {
        emit(Result.Loading("Deleting Article"))

        firestore.collection("Articles")
            .document(articleId)
            .delete()
            .await()

        emit(Result.Success("Deleted"))
    }.catch {
        emit(Result.Error(it))
    }.flowOn(Dispatchers.IO)
}