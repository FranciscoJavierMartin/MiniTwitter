package com.azure.minitwitter.retrofit

import com.azure.minitwitter.retrofit.request.RequestCreateTweet
import com.azure.minitwitter.retrofit.request.RequestUserProfile
import com.azure.minitwitter.retrofit.response.ResponseUploadPhoto
import com.azure.minitwitter.retrofit.response.ResponseUserProfile
import com.azure.minitwitter.retrofit.response.Tweet
import com.azure.minitwitter.retrofit.response.TweetDeleted
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface AuthTwitterService {

    // Tweets
    @GET("tweets/all")
    fun getAllTweets(): Call<List<Tweet>>

    @POST("tweets/create")
    fun createTweet(@Body requestCreateTweet: RequestCreateTweet): Call<Tweet>

    @POST("tweets/like/{idTweet}")
    fun likeTweet(@Path("idTweet") idTweet: Int): Call<Tweet>

    @DELETE("tweets/{idTweet}")
    fun deleteTweet(@Path("idTweet") idTweet: Int): Call<TweetDeleted>

    // Users
    @GET("users/profile")
    fun getProfile(): Call<ResponseUserProfile>

    @PUT("users/profile")
    fun updateProfile(@Body requestUserProfile: RequestUserProfile): Call<ResponseUserProfile>

    @Multipart
    @POST("users/uploadprofilephoto")
    fun uploadProfilePhoto(@Part("file\"; filename=\"photo.jpeg\" ") file: RequestBody): Call<ResponseUploadPhoto>
}