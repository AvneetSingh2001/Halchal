package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Response
import kotlinx.coroutines.flow.Flow

interface UserRespository {

    fun getUserRemotely(uid: String): Flow<Response<User>>

    fun saveUserDataRemotely(user: User)


}