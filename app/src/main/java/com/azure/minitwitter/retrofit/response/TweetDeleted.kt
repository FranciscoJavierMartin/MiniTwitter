package com.azure.minitwitter.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TweetDeleted (
        @SerializedName("message") @Expose val message: String,
        @SerializedName("user") @Expose val user: User
)