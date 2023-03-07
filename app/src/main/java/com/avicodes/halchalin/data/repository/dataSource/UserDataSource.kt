package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.Comment
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    fun getUserLocally() : Flow<User>

    fun getUserRemotely(uid: String): Flow<Result<User>>

    fun saveUserDataRemotely(user: User)

    fun saveUserDataLocally(user: User)

    fun updateUserDataRemotely()

    fun updateUserDataLocally(user: User)

    fun DeleteUserData()

    suspend fun getUserById(userId: String): User?

    fun updateUserPic(image: String, uid: String): Flow<Result<String>>

    fun isLoggedIn() : Flow<Boolean>

    suspend fun login()

    suspend fun logout()
}