package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.flow.Flow
import com.avicodes.halchalin.data.utils.Result

class updateUserPicUseCase ( private val userRespository: UserRespository
) {
    suspend fun execute(img: String, uid: String): Flow<Result<String>> {
        return userRespository.updateUserPic(img, uid)
    }
}