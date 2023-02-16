package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val userRespository: UserRespository
) {
    fun execute(uid: String): Flow<Result<User>> {
        return userRespository.getUserRemotely(uid)
    }
}