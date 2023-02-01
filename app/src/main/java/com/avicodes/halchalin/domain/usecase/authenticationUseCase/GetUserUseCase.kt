package com.avicodes.halchalin.domain.usecase.authenticationUseCase

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.repository.dataSource.UserDataSource
import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val userRespository: UserRespository
) {
    fun execute(uid: String): Flow<Response<User>> {
        return userRespository.getUserRemotely(uid)
    }
}