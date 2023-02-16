package com.avicodes.halchalin.data.repository.dataSourceImpl

import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.repository.dataSource.UserDataSource
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserDataSourceImpl(
    private val auth: FirebaseAuth, private val firestoreDb: FirebaseFirestore
) : UserDataSource {

    override suspend fun getUserLocally() {
        TODO("Not yet implemented")
    }

    override fun getUserRemotely(uid: String) = flow {
        emit(Result.Loading("Loading"))
        try {
            val task = firestoreDb.collection("Users").document(uid).get().await()
            if (task.exists()) {
                emit(Result.Success(task.toObject(User::class.java)))
            } else {
                emit(Result.Success(null))
            }
        } catch (e: java.lang.Exception) {
            emit(Result.Error(e))
        }
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