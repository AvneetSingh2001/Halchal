package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.domain.repository.UserRespository

class UserUploadRemotelyUseCase(
    private val userRespository: UserRespository
) {

    fun execute(user: User) {
        userRespository.saveUserDataRemotely(
            user = user
        )
    }
}