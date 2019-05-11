package com.azure.minitwitter.retrofit.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestSignup(
        @SerializedName("username") @Expose val username: String,
        @SerializedName("email") @Expose val email: String,
        @SerializedName("password") @Expose val password: String,
        @SerializedName("code") @Expose val code: String
)