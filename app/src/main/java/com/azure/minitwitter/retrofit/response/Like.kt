package com.azure.minitwitter.retrofit.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Like (
        @SerializedName("id") @Expose val id: Int,
        @SerializedName("username") @Expose val username: String,
        @SerializedName("description") @Expose val description: String,
        @SerializedName("website") @Expose val website: String,
        @SerializedName("photoUrl") @Expose val photoUrl: String,
        @SerializedName("created") @Expose val created: String
)