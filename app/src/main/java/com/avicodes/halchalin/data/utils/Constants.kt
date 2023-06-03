package com.avicodes.halchalin.data.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val PERMISSION_READ_STORAGE_REQUEST_CODE = 1

    var LOGGEDIN = booleanPreferencesKey("LOGGEDIN")
    var USERID = stringPreferencesKey("USER_ID")
    val NAME = stringPreferencesKey("USER_NAME")
    val LOCATION = stringPreferencesKey("LOCATION")
    val PHONE = stringPreferencesKey("PHONE")
    val IMAGE = stringPreferencesKey("IMAGE")
    val ABOUT = stringPreferencesKey("ABOUT")

}