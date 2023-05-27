package com.avicodes.halchalin.data.repository.article

import android.net.Uri
import com.avicodes.halchalin.data.models.Article
import com.avicodes.halchalin.data.models.ArticleProcessed
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface ArticleDataSource {
    fun uploadArticle(title: String, desc: String, tag: String, imgUri: Uri, userId: String, enableComment: Boolean): Flow<Result<String>>

    fun getAllArticles() : Flow<Result<List<Article>>>

    fun getFeaturedArticles() : Flow<Result<List<Article>>>

    suspend fun createDynamicLink(
        article: ArticleProcessed
    ) : Flow<Result<String>>

    fun getArticleById(articleId: String): Flow<Result<Article>>

    fun getUserArticles(userId: String): Flow<Result<List<Article>>>

    fun deleteArticle(articleId: String) : Flow<Result<String>>

}