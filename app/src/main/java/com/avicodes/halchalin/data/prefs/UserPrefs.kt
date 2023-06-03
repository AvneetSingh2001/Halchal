package com.avicodes.halchalin.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.avicodes.halchalin.data.models.User
import com.avicodes.halchalin.data.utils.Constants.ABOUT
import com.avicodes.halchalin.data.utils.Constants.IMAGE
import com.avicodes.halchalin.data.utils.Constants.LOCATION
import com.avicodes.halchalin.data.utils.Constants.LOGGEDIN
import com.avicodes.halchalin.data.utils.Constants.NAME
import com.avicodes.halchalin.data.utils.Constants.PHONE
import com.avicodes.halchalin.data.utils.Constants.USERID
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPrefs(
    private val dataStore: DataStore<Preferences>
){

    suspend fun saveUser(user: User) {
        dataStore.edit {
            it[USERID] = user.userId
            it[NAME] = user.name
            it[LOCATION] = user.location
            it[PHONE] = user.mobile
            it[IMAGE] = user.imgUrl
            it[ABOUT] = user.about
        }
    }

    suspend fun login() {
        dataStore.edit {
            it[LOGGEDIN] = true
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it[LOGGEDIN] = false
        }
    }

    fun getUser() = dataStore.data.map {
            User(
                it[USERID]?:"",
                it[NAME]?:"",
                it[LOCATION]?:"",
                it[PHONE]?:"" ,
                it[IMAGE]?:"",
                it[ABOUT]?:""
            )
        }

    fun isLoggedIn() = dataStore.data.map {
        it[LOGGEDIN] ?: false
    }

    fun getUserId() = dataStore.data.map {
        it[USERID]?:""
    }

    fun getUserName() = dataStore.data.map {
        it[NAME]?:""
    }

    fun getUserLocation() = dataStore.data.map {
        it[LOCATION]?:""
    }

    fun getUserPhone() = dataStore.data.map {
        it[PHONE]?:""
    }

    fun getUserImage() = dataStore.data.map {
        it[IMAGE]?:""
    }
}