package com.azure.minitwitter.retrofit;

import com.azure.minitwitter.RequestLogin;
import com.azure.minitwitter.RequestSignup;
import com.azure.minitwitter.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {

    @POST("/auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    @POST("/auth/signup")
    Call<ResponseAuth> doSignUp(@Body RequestSignup requestSignup);
}
