package com.avicodes.halchalin.data.repository

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.repository.dataSource.UserDataSource
import com.avicodes.halchalin.data.utils.Result
import com.avicodes.halchalin.domain.repository.UserRespository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRespository{

    override fun getUserRemotely(uid: String): Flow<Result<User>> {
        return userDataSource.getUserRemotely(uid)
    }

    override fun saveUserDataRemotely(user: User) {
        userDataSource.saveUserDataLocally(user)
    }

    override suspend fun getUserById(uid: String): User? {
        return userDataSource.getUserById(uid)
    }

    override fun updateUserPic(img: String): Flow<Result<String>> {
        return userDataSource.updateUserPic(img)
    }

    override suspend fun saveUserLocally(user: User) {
        userDataSource.saveUserDataLocally(user)
    }

    override fun getUserLocally(): Flow<User> {
        return userDataSource.getUserLocally()
    }


}