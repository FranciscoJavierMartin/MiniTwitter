package com.azure.minitwitter.retrofit.response;

import com.azure.minitwitter.common.Constants;
import com.azure.minitwitter.retrofit.AuthInterceptor;
import com.azure.minitwitter.retrofit.AuthTwitterService;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {

    private static AuthTwitterClient instance = null;
    private AuthTwitterService authTwitterService;
    private Retrofit retrofit;

    private AuthTwitterClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient client = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        authTwitterService = retrofit.create(AuthTwitterService.class);
    }

    public static AuthTwitterClient getInstance(){
        if(instance == null){
            instance = new AuthTwitterClient();
        }

        return instance;
    }

    public AuthTwitterService getAuthTwitterService() {
        return authTwitterService;
    }
}
