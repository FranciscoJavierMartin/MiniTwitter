package com.azure.minitwitter.retrofit.response

import com.azure.minitwitter.common.Constants
import com.azure.minitwitter.retrofit.AuthInterceptor
import com.azure.minitwitter.retrofit.AuthTwitterService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthTwitterClient private constructor() {

    var authTwitterService: AuthTwitterService

    init {
        val okHttpClientBuilder = OkHttpClient.Builder()
        okHttpClientBuilder.addInterceptor(AuthInterceptor())
        val client = okHttpClientBuilder.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        this.authTwitterService = retrofit.create(AuthTwitterService::class.java)
    }

    companion object {
        val instance: AuthTwitterClient by lazy { AuthTwitterClient() }
    }
}

