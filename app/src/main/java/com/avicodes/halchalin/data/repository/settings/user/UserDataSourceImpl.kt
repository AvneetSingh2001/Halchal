package com.avicodes.halchalin.data.repository.settings.user

import androidx.core.net.toUri
import com.avicodes.halchalin.data.models.Admin
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.prefs.UserPrefs
import com.avicodes.halchalin.data.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class UserDataSourceImpl(
    private val auth: FirebaseAuth,
    private val firestoreDb: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val userPrefs: UserPrefs
) : UserDataSource {

    override fun getUserLocally(): Flow<User> {
        return userPrefs.getUser()
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
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun saveUserDataRemotely(user: User) {
        auth.uid?.let { uid ->
            user.userId = uid
            firestoreDb.collection("Users").document(user.userId).set(user)
        }
    }


    override suspend fun getUserById(userId: String): User? {
        val task = firestoreDb.collection("Users").document(userId).get().await()
        val user = task.toObject(User::class.java)
        return user
    }

    override fun updateUserPic(image: String, uid: String) = flow {
        emit(Result.Loading("Updating"))
        val it = firebaseStorage.getReference("dp/$uid").putFile(
            image.toUri()
        ).await()
        val task = it.storage.downloadUrl.await()
        val imgUrl = task.toString()
        emit(Result.Success(imgUrl))
    }.catch { emit(Result.Error(it)) }.flowOn(Dispatchers.IO)

    override fun isLoggedIn(): Flow<Boolean> {
        return userPrefs.isLoggedIn()
    }

    override suspend fun login() {
        userPrefs.login()
    }

    override suspend fun logout() {
        userPrefs.logout()
    }

    override fun saveUserDataLocally(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            auth.uid?.let { uid ->
                user.userId = uid
                userPrefs.saveUser(user)
            }
        }
    }


}