package com.avicodes.halchalin.domain.repository

import com.avicodes.halchalin.data.models.User

interface UserRespository {

    suspend fun getUserRemotely(uid: String)

    fun saveUserDataRemotely(user: User)
    
}