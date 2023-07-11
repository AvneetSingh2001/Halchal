package com.avicodes.halchalin.data.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val PERMISSION_READ_STORAGE_REQUEST_CODE = 1

    var LOGGEDIN = booleanPreferencesKey("LOGGEDIN")
    var USERID = stringPreferencesKey("USER_ID")
    val NAME = stringPreferencesKey("USER_NAME")
    val LOCATION = stringPreferencesKey("LOCATION")
    val IMAGE = stringPreferencesKey("IMAGE")
    val ABOUT = stringPreferencesKey("ABOUT")

    const val CONTACT_US_URI = "https://www.kichhakihalchal.com/contact"
    const val PRIVACY_POLICY_URI = "https://www.kichhakihalchal.com/privacy-policy"
    const val REQUEST_FEATURE_URI = "https://forms.gle/7ak1YmQZWDyGMt1W7"
    const val ABOUT_US_URI = "https://www.kichhakihalchal.com/privacy-policy"
    const val JOIN_US_URI = "https://forms.gle/xdCh9RRVNz3UJrJn7"
    const val BUG_URI = "https://forms.gle/Y6Efj3A5rM8zCtag9"

    const val APP_PACKAGE_NAME = "com.avicodes.halchalin"
}