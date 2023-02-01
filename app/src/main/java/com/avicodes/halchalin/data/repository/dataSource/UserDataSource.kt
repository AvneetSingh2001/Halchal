package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Response
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    suspend fun getUserLocally()

    fun getUserRemotely(uid: String): Flow<Response<User>>

    fun saveUserDataRemotely(user: User)

    fun saveUserDataLocally()

    fun updateUserDataRemotely()

    fun updateUserDataLocally()

    fun DeleteUserData()
}