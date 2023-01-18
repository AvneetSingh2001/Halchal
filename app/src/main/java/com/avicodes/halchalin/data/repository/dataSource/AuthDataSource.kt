package com.avicodes.halchalin.data.repository.dataSource

import com.avicodes.halchalin.data.utils.Response
import com.google.firebase.auth.FirebaseUser

interface AuthDataSource {


    suspend fun login(contact: String) : Response

    suspend fun deleteUser(contact: String)

    fun logout()
}