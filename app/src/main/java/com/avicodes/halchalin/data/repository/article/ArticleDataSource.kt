package com.avicodes.halchalin.data.repository.article

import android.net.Uri
import com.avicodes.halchalin.data.models.Article
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface ArticleDataSource {
    fun uploadArticle(title: String, desc: String, tag: String, imgUri: Uri): Flow<Result<String>>
}