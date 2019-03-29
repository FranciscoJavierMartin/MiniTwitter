package com.azure.minitwitter.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.azure.minitwitter.MyTweetRecyclerViewAdapter;
import com.azure.minitwitter.common.MyApp;
import com.azure.minitwitter.retrofit.AuthTwitterService;
import com.azure.minitwitter.retrofit.response.AuthTwitterClient;
import com.azure.minitwitter.retrofit.response.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    LiveData<List<Tweet>> allTweets;

    public TweetRepository(){
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
    }

    public LiveData<List<Tweet>> getAllTweets() {
        final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();

        Call<List<Tweet>> call = authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                   data.setValue(response.body());


                }else{
                    Toast.makeText(MyApp.getContext(),"Something goes wrong", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error on connection", Toast.LENGTH_SHORT);
            }
        });

        return data;
    }
}
