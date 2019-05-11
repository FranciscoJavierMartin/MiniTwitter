package com.azure.minitwitter.common

object Constants{
    const val API_MINITWITTER_BASE_URL = "https://minitwitter.com:3001/apiv1/"
    const val API_MINITWITTER_FILES_URL = "https://minitwitter.com/apiv1/uploads/photos/"

    // startActivityForResult
    const val SELECT_PHOTO_GALLERY = 1

    // Preferences
    const val PREF_TOKEN = "PREF_TOKEN"
    const val PREF_USERNAME = "PREF_USERNAME"
    const val PREF_EMAIL = "PREF_EMAIL"
    const val PREF_PHOTOURL = "PREF_PHOTOURL"
    const val PREF_CREATED = "PREF_CREATED"
    const val PREF_ACTIVE = "PREF_ACTIVE"

    // Arguments
    const val TWEET_LIST_TYPE = "TWEET_LIST_TYPE"
    const val TWEET_LIST_ALL = 1
    const val TWEET_LIST_FAVS = 2
    const val ARG_TWEET_ID = "ARG_TWEET_ID"
}