package com.azure.minitwitter.data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.azure.minitwitter.retrofit.request.RequestUserProfile

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    val profileRepository = ProfileRepository()
    val userProfile = profileRepository.getProfile()
    val photoProfile: LiveData<String> = profileRepository.photoProfile

    fun updateProfile(requestUserProfile: RequestUserProfile){
        profileRepository.updateProfile(requestUserProfile)
    }

    fun uploadPhoto(photo: String){
        profileRepository.uploadPhoto(photo)
    }
}