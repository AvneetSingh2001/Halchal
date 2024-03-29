package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.Admin
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserRespository {

    fun getUserRemotely(uid: String): Flow<Result<User>>

    fun saveUserDataRemotely(user: User)

    suspend fun getUserById(uid: String): User?

    fun updateUserPic(img: String, uid: String): Flow<Result<String>>

    suspend fun saveUserLocally(user: User)

    fun getUserLocally(): Flow<User>

    fun saveUser(user: User)

    fun isLoggedIn(): Flow<Boolean>

    suspend fun login()

    suspend fun logout()
}