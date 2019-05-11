package com.azure.minitwitter.data

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.azure.minitwitter.retrofit.response.Tweet
import com.azure.minitwitter.ui.tweets.BottomModalTweetFragment

class TweetViewModel(application: Application) : AndroidViewModel(application){

    private val tweetRepository: TweetRepository = TweetRepository()

    private var tweets = tweetRepository.getAllTweets()
//        get() = tweets

    private val favTweets : LiveData<List<Tweet>> = tweetRepository.getFavTweets()
//        get() = favTweets

    fun getNewTweets(): MutableLiveData<List<Tweet>> {
        tweets = tweetRepository.getAllTweets()
        return tweets
    }

    fun getNewFavTweets(): LiveData<List<Tweet>> {
        getNewTweets()
        return favTweets
    }

    fun insertTweet(message: String): Unit = tweetRepository.createTweet(message)

    fun likeTweet(idTweet: Int): Unit  = tweetRepository.likeTweet(idTweet)

    fun deleteTweet(idTweet: Int): Unit = tweetRepository.deleteTweet(idTweet)

    fun openDialogTweetMenu(ctx: Context, idTweet: Int){
        val dialogTweet: BottomModalTweetFragment = BottomModalTweetFragment.newInstance(idTweet)
        dialogTweet.show((ctx as AppCompatActivity).supportFragmentManager, "BottomModalTweetFragment")
    }
}