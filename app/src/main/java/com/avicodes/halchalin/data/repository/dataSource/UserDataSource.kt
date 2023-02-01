package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.models.User

interface UserDataSource {

    suspend fun getUserLocally()

    suspend fun getUserRemotely(uid: String)

    fun saveUserDataRemotely(user: User)

    fun saveUserDataLocally()

    fun updateUserDataRemotely()

    fun updateUserDataLocally()

    fun DeleteUserData()
}