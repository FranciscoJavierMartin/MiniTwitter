package com.azure.minitwitter.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.azure.minitwitter.MyTweetRecyclerViewAdapter;
import com.azure.minitwitter.common.MyApp;
import com.azure.minitwitter.retrofit.AuthTwitterService;
import com.azure.minitwitter.retrofit.request.RequestCreateTweet;
import com.azure.minitwitter.retrofit.response.AuthTwitterClient;
import com.azure.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {

    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<List<Tweet>> allTweets;

    public TweetRepository(){
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
    }

    public MutableLiveData<List<Tweet>> getAllTweets() {

        if(allTweets == null){
            allTweets = new MutableLiveData<>();
        }

        Call<List<Tweet>> call = authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                    allTweets.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(),"Something goes wrong", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error on connection", Toast.LENGTH_SHORT);
            }
        });

        return allTweets;
    }

    public void createTweet(String message){
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(message);
        Call<Tweet> call = authTwitterService.createTweet(requestCreateTweet);

        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listCloned = new ArrayList<>();
                    listCloned.add(response.body());

                    for(Tweet tweet: allTweets.getValue()){
                        listCloned.add(new Tweet(tweet));
                    }

                    allTweets.setValue(listCloned);
                } else {
                    // TODO: Refactor to include the error message on string
                    Toast.makeText(MyApp.getContext(), "Something goes wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                // TODO: Refactor to include the error message on string
                Toast.makeText(MyApp.getContext(), "Error on connection",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
