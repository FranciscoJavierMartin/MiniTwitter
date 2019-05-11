package com.azure.minitwitter.retrofit.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestCreateTweet(
        @SerializedName("message") @Expose val message: String
)