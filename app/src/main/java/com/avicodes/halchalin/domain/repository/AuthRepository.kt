package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.utils.Response
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {


    suspend fun login(contact: String) : Response
    suspend fun deleteUser(contact: String)
    fun logout()

}