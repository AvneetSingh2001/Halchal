package com.avicodes.halchalin.data.repository.article

import android.net.Uri
import com.avicodes.halchalin.data.models.Article
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.Flow

class ArticleRepositoryImpl(
    private val articleDataSource: ArticleDataSource
) : ArticleRepository {
    override fun uploadArticle(title: String, desc: String, tag: String, imgUri: Uri): Flow<Result<String>> {
        return articleDataSource.uploadArticle(title, desc, tag, imgUri)
    }

    override fun getAllArticles(): Flow<Result<List<Article>>> {
        return articleDataSource.getAllArticles()
    }

    override fun getFeaturedArticles(): Flow<Result<List<Article>>> {
        return articleDataSource.getFeaturedArticles()
    }
}