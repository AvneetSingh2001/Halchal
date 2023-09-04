package com.avicodes.halchalin.data.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val PERMISSION_READ_STORAGE_REQUEST_CODE = 1
    const val PERMISSION_NOTFICATION_REQUEST_CODE = 122
    const val PERMISSION_CAMERA_REQUEST_CODE = 122

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


    //events
    const val ANALYTICS_BOTTOM_NAV_LOCAL = "ANALYTICS_BOTTOM_NAV_LOCAL"
    const val ANALYTICS_BOTTOM_NAV_HALL = "ANALYTICS_BOTTOM_NAV_HALL"
    const val ANALYTICS_BOTTOM_NAV_EXPLORE = "ANALYTICS_BOTTOM_NAV_EXPLORE"
    const val ANALYTICS_BOTTOM_NAV_PROFILE = "ANALYTICS_BOTTOM_NAV_PROFILE"
    const val ANALYTICS_NEWS_COMMENTS = "ANALYTICS_NEWS_COMMENTS"
    const val ANALYTICS_ARTICLE_COMMENTS = "ANALYTICS_ARTICLE_COMMENTS"
    const val ANALYTICS_ARTICLE_SHARE = "ANALYTICS_ARTICLE_SHARE"
    const val ANALYTICS_NEWS_SHARE = "ANALYTICS_NEWS_SHARE"
    const val ANALYTICS_ARTICLE_OPEN = "ANALYTICS_ARTICLE_OPEN"
    const val ANALYTICS_NEWS_OPEN = "ANALYTICS_NEWS_OPEN"
    const val ANALYTICS_API_NEWS_OPEN = "ANALYTICS_API_NEWS_OPEN"
    const val ANALYTICS_CHANGE_DETAILS = "ANALYTICS_CHANGE_DETAILS"
    const val ANALYTICS_MY_ARTICLES = "ANALYTICS_MY_ARTICLES"
    const val ANALYTICS_OUR_COMMUNITY = "ANALYTICS_OUR_COMMUNITY"
    const val ANALYTICS_RATE_US = "ANALYTICS_RATE_US"
    const val ANALYTICS_WRITE_ARTICLE = "ANALYTICS_WRITE_ARTICLE"

}