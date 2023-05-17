package com.avicodes.halchalin.domain.repository

import android.net.Uri
import com.avicodes.halchalin.data.models.Article
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun uploadArticle(title: String, desc: String, tag: String, imgUri: Uri): Flow<Result<String>>

    fun getAllArticles() : Flow<Result<List<Article>>>

    fun getFeaturedArticles() : Flow<Result<List<Article>>>

}

