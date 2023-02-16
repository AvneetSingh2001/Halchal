package com.avicodes.halchalin.data.repository

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.repository.dataSource.UserDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRespository{

    override fun getUserRemotely(uid: String): Flow<Result<User>> {
        return userDataSource.getUserRemotely(uid)
    }

    override fun saveUserDataRemotely(user: User) {
        userDataSource.saveUserDataRemotely(user)
    }


}