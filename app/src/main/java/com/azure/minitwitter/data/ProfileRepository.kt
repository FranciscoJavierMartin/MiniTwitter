package com.azure.minitwitter.data

import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import com.azure.minitwitter.common.Constants
import com.azure.minitwitter.common.MyApp
import com.azure.minitwitter.common.SharedPreferencesManager
import com.azure.minitwitter.retrofit.AuthTwitterService
import com.azure.minitwitter.retrofit.request.RequestUserProfile
import com.azure.minitwitter.retrofit.response.AuthTwitterClient
import com.azure.minitwitter.retrofit.response.ResponseUploadPhoto
import com.azure.minitwitter.retrofit.response.ResponseUserProfile
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileRepository{
    val authTwitterClient: AuthTwitterClient = AuthTwitterClient.instance
    val authTwitterService: AuthTwitterService = authTwitterClient.authTwitterService
    var userProfile: MutableLiveData<ResponseUserProfile> = getProfile()
    var photoProfile: MutableLiveData<String> = MutableLiveData()

    fun getProfile(): MutableLiveData<ResponseUserProfile> {

        if(userProfile == null){
            userProfile = MutableLiveData()
        }

        val call = authTwitterService.getProfile()
        call.enqueue(object :Callback<ResponseUserProfile>{
            override fun onResponse(call: Call<ResponseUserProfile>, response: Response<ResponseUserProfile>) {
                if(response.isSuccessful){
                    userProfile.value = response.body()
                } else {
                    // TODO: Make a better solution
                    Toast.makeText(MyApp.context, "Something has gone wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseUserProfile>, t: Throwable) {
                // TODO: Make a better solution
                Toast.makeText(MyApp.context, "Error on connection", Toast.LENGTH_SHORT).show()
            }
        })

        return userProfile
    }

    fun updateProfile(requestUserProfile: RequestUserProfile): MutableLiveData<ResponseUserProfile>{
        val call = authTwitterService.updateProfile(requestUserProfile)

        call.enqueue(object: Callback<ResponseUserProfile>{
            override fun onResponse(call: Call<ResponseUserProfile>, response: Response<ResponseUserProfile>) {
                if(response.isSuccessful){
                    userProfile.value = response.body()
                } else {
                    // TODO: Make a better solution
                    Toast.makeText(MyApp.context, "Something has gone wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseUserProfile>, t: Throwable) {
                // TODO: Make a better solution
                Toast.makeText(MyApp.context, "Error on connection", Toast.LENGTH_SHORT).show()
            }
        })

        return userProfile
    }

    fun uploadPhoto(photoPath: String){
        val file = File(photoPath)
        val requestBody = RequestBody.create(MediaType.parse("image/jpg"), file)
        val call = authTwitterService.uploadProfilePhoto(requestBody)

        call.enqueue(object: Callback<ResponseUploadPhoto>{
            override fun onResponse(call: Call<ResponseUploadPhoto>, response: Response<ResponseUploadPhoto>) {
                if(response.isSuccessful){
                    SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body()!!.filename)
                    photoProfile.value = response.body()!!.filename
                } else {
                    Toast.makeText(MyApp.context, "Something has gone wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseUploadPhoto>, t: Throwable) {
                Toast.makeText(MyApp.context, "Error on connection", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

