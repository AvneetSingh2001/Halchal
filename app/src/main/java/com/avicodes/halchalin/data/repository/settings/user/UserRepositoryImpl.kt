package com.avicodes.halchalin.data.repository.settings.user

import com.avicodes.halchalin.data.models.User
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

    override suspend fun getUserById(uid: String): User? {
        return userDataSource.getUserById(uid)
    }

    override fun updateUserPic(img: String, uid: String): Flow<Result<String>> {
        return userDataSource.updateUserPic(img, uid)
    }

    override suspend fun saveUserLocally(user: User) {
        userDataSource.saveUserDataLocally(user)
    }

    override fun getUserLocally(): Flow<User> {
        return userDataSource.getUserLocally()
    }

    override fun saveUser(user: User) {
        userDataSource.saveUserDataRemotely(user)
        userDataSource.saveUserDataLocally(user)
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return userDataSource.isLoggedIn()
    }

    override suspend fun login() {
        userDataSource.login()
    }

    override suspend fun logout() {
        userDataSource.logout()
    }


}