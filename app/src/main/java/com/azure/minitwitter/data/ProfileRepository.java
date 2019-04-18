package com.azure.minitwitter.data;

import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.azure.minitwitter.common.Constants;
import com.azure.minitwitter.common.MyApp;
import com.azure.minitwitter.common.SharedPreferencesManager;
import com.azure.minitwitter.retrofit.AuthTwitterService;
import com.azure.minitwitter.retrofit.request.RequestUserProfile;
import com.azure.minitwitter.retrofit.response.AuthTwitterClient;
import com.azure.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.azure.minitwitter.retrofit.response.ResponseUserProfile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    AuthTwitterClient authTwitterClient;
    AuthTwitterService authTwitterService;
    MutableLiveData<ResponseUserProfile> userProfile;
    MutableLiveData<String> photoProfile;

    ProfileRepository(){
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        userProfile = getProfile();
        if(photoProfile == null){
            photoProfile = new MutableLiveData<>();
        }
    }

    public MutableLiveData<String> getPhotoProfile(){
        return photoProfile;
    }

    public MutableLiveData<ResponseUserProfile> getProfile() {

        if(userProfile == null){
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = authTwitterService.getProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                } else {
                    // TODO: Make a better solution
                    Toast.makeText(MyApp.getContext(), "Something has gone wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                // TODO: Make a better solution
                Toast.makeText(MyApp.getContext(), "Error on connection", Toast.LENGTH_SHORT).show();
            }
        });

        return userProfile;
    }

    public MutableLiveData<ResponseUserProfile> updateProfile(RequestUserProfile requestUserProfile){
        Call<ResponseUserProfile> call = authTwitterService.updateProfile(requestUserProfile);

        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                } else {
                    // TODO: Make a better solution
                    Toast.makeText(MyApp.getContext(), "Something has gone wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                // TODO: Make a better solution
                Toast.makeText(MyApp.getContext(), "Error on connection", Toast.LENGTH_SHORT).show();
            }
        });

        return userProfile;
    }

    public void uploadPhoto(final String photoPath){
        File file = new File(photoPath);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<ResponseUploadPhoto> call = authTwitterService.uploadProfilePhoto(requestBody);

        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if(response.isSuccessful()){
                    SharedPreferencesManager.setSomeStringValue(Constants.PREF_PHOTOURL, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());
                } else {
                    Toast.makeText(MyApp.getContext(), "Something has gone wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error on connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
