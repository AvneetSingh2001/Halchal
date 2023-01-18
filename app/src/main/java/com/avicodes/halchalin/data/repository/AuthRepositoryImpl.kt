package com.avicodes.halchalin.data.repository

import com.avicodes.halchalin.data.repository.dataSource.AuthDataSource
import com.avicodes.halchalin.data.utils.Response
import com.avicodes.halchalin.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {

    override suspend fun login(contact: String): Response {
        return authDataSource.login(contact)
    }

    override suspend fun deleteUser(contact: String) {
        authDataSource.deleteUser(contact)
    }

    override fun logout() {
        authDataSource.logout()
    }

}