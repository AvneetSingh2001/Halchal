package com.avicodes.halchalin.data.repository

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.repository.dataSource.UserDataSource
import com.avicodes.halchalin.domain.repository.UserRespository

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRespository{

    override suspend fun getUserRemotely(uid: String) {
        userDataSource.getUserRemotely(uid)
    }

    override fun saveUserDataRemotely(user: User) {
        userDataSource.saveUserDataRemotely(user)
    }


}