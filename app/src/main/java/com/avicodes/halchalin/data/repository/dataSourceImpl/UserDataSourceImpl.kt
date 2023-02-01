package com.avicodes.halchalin.data.repository.dataSourceImpl

import androidx.lifecycle.LiveData
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.repository.dataSource.UserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserDataSourceImpl(
    private val auth: FirebaseAuth,
    private val firestoreDb: FirebaseFirestore
): UserDataSource {


    override suspend fun getUserLocally() {
        TODO("Not yet implemented")
    }

    override suspend fun getUserRemotely(uid: String) {
        firestoreDb.collection("Users").document(uid).get()
    }

    override fun saveUserDataRemotely(user: User) {
        firestoreDb.collection("Users").document(user.userId).set(user)
    }

    override fun saveUserDataLocally() {
        TODO("Not yet implemented")
    }

    override fun updateUserDataRemotely() {
        TODO("Not yet implemented")
    }

    override fun updateUserDataLocally() {
        TODO("Not yet implemented")
    }

    override fun DeleteUserData() {
        TODO("Not yet implemented")
    }
}