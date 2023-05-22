package com.avicodes.halchalin.domain.repository

import android.net.Uri
import com.avicodes.halchalin.data.models.Article
import com.avicodes.halchalin.data.models.ArticleProcessed
import com.avicodes.halchalin.data.models.News
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun uploadArticle(title: String, desc: String, tag: String, imgUri: Uri, userId: String): Flow<Result<String>>

    fun getAllArticles() : Flow<Result<List<Article>>>

    fun getFeaturedArticles() : Flow<Result<List<Article>>>

    suspend fun createArticleDynamicLink(
        article: ArticleProcessed
    ): Flow<Result<String>>

    fun getArticleById(articleId: String): Flow<Result<Article>>

    fun getUserArticles(userId: String) : Flow<Result<List<Article>>>
}

