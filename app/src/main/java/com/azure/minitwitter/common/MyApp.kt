package com.azure.minitwitter.common

import android.app.Application
import android.content.Context

class MyApp : Application() {

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        var instance: MyApp? = null
            private set

        val context: Context?
            get() = instance
    }
}