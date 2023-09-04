package com.avicodes.halchalin.domain.repository

import android.net.Uri
import com.avicodes.halchalin.data.models.News
import kotlinx.coroutines.flow.Flow
import com.avicodes.halchalin.data.utils.Result

interface AdminRepository {
    suspend fun checkIsAdmin(): Boolean
    fun uploadNews(
        newsHeadline: String,
        newsDesc: String,
        newsLoc: String,
        videoUri: Uri?,
        uploadedImages: List<Uri>,
        coverUri: Uri?
    ): Flow<Result<String>>
}