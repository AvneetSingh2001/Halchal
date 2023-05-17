package com.avicodes.halchalin.data.repository.article

import android.net.Uri
import androidx.core.net.toUri
import com.avicodes.halchalin.data.models.Article
import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.firestore.Query
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

    override fun uploadArticle(title: String, desc: String, tag: String, imgUri: Uri) =
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

            userPrefs.getUserId().collectLatest { userId ->
                var article = Article(
                    articleImage = articleUrl,
                    articleId = id,
                    articleDesc = desc,
                    articleTag = tag,
                    articleTitle = title,
                    date = date,
                    userId = userId
                )
                firestore.collection("Articles").document(article.articleId)
                    .set(article).await()

                emit(Result.Success("Uploaded"))
            }
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
}