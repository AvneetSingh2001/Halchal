package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.repository.dataSource.AuthDataSource
import com.avicodes.halchalin.data.utils.Response
import com.google.firebase.auth.*

class AuthDataSourceImpl(private val firebaseAuth: FirebaseAuth) : AuthDataSource {



    override suspend fun login(contact: String): Response {
        TODO()
    }

    override suspend fun deleteUser(contact: String) {

    }

    override fun logout() {
        firebaseAuth.signOut()
    }

}