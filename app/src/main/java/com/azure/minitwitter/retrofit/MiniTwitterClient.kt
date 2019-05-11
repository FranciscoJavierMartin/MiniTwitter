package com.azure.minitwitter.retrofit

import com.azure.minitwitter.common.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MiniTwitterClient private constructor(){

    var miniTwitterService: MiniTwitterService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        miniTwitterService = retrofit.create(MiniTwitterService::class.java)
    }

    companion object{
        val instance: MiniTwitterClient by lazy { MiniTwitterClient() }
    }
}