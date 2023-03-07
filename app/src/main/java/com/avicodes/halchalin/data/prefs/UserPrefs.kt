package com.avicodes.halchalin.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.avicodes.halchalin.data.models.User
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPrefs(
    private val dataStore: DataStore<Preferences>
){


    companion object {
        var LOGGEDIN = booleanPreferencesKey("LOGGEDIN")
        var USERID = stringPreferencesKey("USER_ID")
        val NAME = stringPreferencesKey("USER_NAME")
        val LOCATION = stringPreferencesKey("LOCATION")
        val PHONE = stringPreferencesKey("PHONE")
        val IMAGE = stringPreferencesKey("IMAGE")
    }

    suspend fun saveUser(user: User) {
        dataStore.edit {
            it[USERID] = user.userId
            it[NAME] = user.name
            it[LOCATION] = user.location
            it[PHONE] = user.mobile
            it[IMAGE] = user.imgUrl
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