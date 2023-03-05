package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.flow.Flow

class GetUserByIdUseCase(
    private val userRespository: UserRespository
) {
    suspend fun execute(uid: String): User? {
        return userRespository.getUserById(uid)
    }
}