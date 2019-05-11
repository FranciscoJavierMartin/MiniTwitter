package com.azure.minitwitter.retrofit

import com.azure.minitwitter.retrofit.request.RequestLogin
import com.azure.minitwitter.retrofit.request.RequestSignup
import com.azure.minitwitter.retrofit.response.ResponseAuth
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MiniTwitterService {

    @POST("auth/login")
    fun doLogin(@Body requestLogin: RequestLogin): Call<ResponseAuth>

    @POST("auth/signup")
    fun doSignUp(@Body requestSignup: RequestSignup): Call<ResponseAuth>
}