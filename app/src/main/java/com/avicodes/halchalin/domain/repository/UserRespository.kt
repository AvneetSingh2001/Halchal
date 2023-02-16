package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserRespository {

    fun getUserRemotely(uid: String): Flow<Result<User>>

    fun saveUserDataRemotely(user: User)


}