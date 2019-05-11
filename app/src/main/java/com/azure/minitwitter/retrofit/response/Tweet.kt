package com.azure.minitwitter.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Tweet (
        @SerializedName("id") @Expose val id: Int,
        @SerializedName("message") @Expose val message: String,
        @SerializedName("likes") @Expose val likes: List<Like> = ArrayList<Like>(),
        @SerializedName("user") @Expose val user: User
) {
    constructor(tweet: Tweet): this(tweet.id, tweet.message, tweet.likes, tweet.user)
}