package com.avicodes.halchalin.data.repository.article

import android.net.Uri
import com.avicodes.halchalin.data.models.Article
import com.avicodes.halchalin.data.models.ArticleProcessed
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class ArticleRepositoryImpl(
    private val articleDataSource: ArticleDataSource
) : ArticleRepository {
    override fun uploadArticle(title: String, desc: String, tag: String, imgUri: Uri, userId: String, enableComment: Boolean): Flow<Result<String>> {
        return articleDataSource.uploadArticle(title, desc, tag, imgUri, userId, enableComment)
    }

    override fun getAllArticles(): Flow<Result<List<Article>>> {
        return articleDataSource.getAllArticles()
    }

    override fun getFeaturedArticles(): Flow<Result<List<Article>>> {
        return articleDataSource.getFeaturedArticles()
    }

    override suspend fun createArticleDynamicLink(article: ArticleProcessed): Flow<Result<String>> {
        return articleDataSource.createDynamicLink(article)
    }

    override fun getArticleById(articleId: String): Flow<Result<Article>> {
        return articleDataSource.getArticleById(articleId)
    }

    override fun getUserArticles(userId: String): Flow<Result<List<Article>>> {
        return articleDataSource.getUserArticles(userId = userId)
    }

    override fun deleteArticle(articleId: String): Flow<Result<String>> {
        return articleDataSource.deleteArticle(articleId = articleId)
    }
}