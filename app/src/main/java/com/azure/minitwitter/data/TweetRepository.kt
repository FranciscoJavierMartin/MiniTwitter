package com.azure.minitwitter.data

import android.arch.lifecycle.MutableLiveData
import android.widget.Toast
import com.azure.minitwitter.common.Constants
import com.azure.minitwitter.common.MyApp
import com.azure.minitwitter.common.SharedPreferencesManager
import com.azure.minitwitter.retrofit.AuthTwitterService
import com.azure.minitwitter.retrofit.request.RequestCreateTweet
import com.azure.minitwitter.retrofit.response.AuthTwitterClient
import com.azure.minitwitter.retrofit.response.Tweet
import com.azure.minitwitter.retrofit.response.TweetDeleted
import retrofit2.Call
import retrofit2.Callback;
import retrofit2.Response

class TweetRepository{

    val authTwitterClient: AuthTwitterClient = AuthTwitterClient.instance
    val authTwitterService: AuthTwitterService = authTwitterClient.authTwitterService
    private var allTweets: MutableLiveData<List<Tweet>> = getAllTweets()
    private lateinit var favTweets: MutableLiveData<List<Tweet>>
    val username = SharedPreferencesManager.getSomeStringValue(Constants.PREF_USERNAME)

    fun getAllTweets(): MutableLiveData<List<Tweet>>{

        if(allTweets == null){
            allTweets = MutableLiveData()
        }

        val call = authTwitterService.getAllTweets()
        call.enqueue(object : Callback<List<Tweet>>{
            override fun onResponse(call: Call<List<Tweet>>, response: Response<List<Tweet>>) {
                if(response.isSuccessful){
                    allTweets.setValue(response.body())
                } else {
                    Toast.makeText(MyApp.context, "Something goes wrong", Toast.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<List<Tweet>>, t: Throwable) {
                Toast.makeText(MyApp.context, "Error on connection", Toast.LENGTH_SHORT)
            }
        })

        return allTweets
    }

    fun getFavTweets(): MutableLiveData<List<Tweet>>{

        if(favTweets == null){
            favTweets = MutableLiveData()
        }

        val newFavList = ArrayList<Tweet>()
        val iterator = allTweets.value!!.iterator()

        while(iterator.hasNext()){
            val current = iterator.next()
            val itLikes = current.likes.iterator()
            var found = false

            while(itLikes.hasNext() && !found){
                val like = itLikes.next()

                if(like.username == username){
                    found = true
                    newFavList.add(current)
                }
            }
        }

        favTweets.value = newFavList
        return favTweets
    }

    fun createTweet(message: String){
        val requestCreateTweet = RequestCreateTweet(message)
        val call = authTwitterService.createTweet(requestCreateTweet)

        call.enqueue(object : Callback<Tweet>{
            override fun onResponse(call: Call<Tweet>, response: Response<Tweet>) {
                if(response.isSuccessful){
                    val listClonned = ArrayList<Tweet>()
                    listClonned.add(response.body()!!)

                    allTweets.value?.forEach { tweet: Tweet ->
                        listClonned.add(Tweet(tweet))
                    }

                    allTweets.value = listClonned
                } else {
                    // TODO: Refactor to include the error message on string
                    Toast.makeText(MyApp.context, "Something goes wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Tweet>, t: Throwable) {
                // TODO: Refactor to include the error message on string
                Toast.makeText(MyApp.context, "Error on connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun likeTweet(idTweet: Int){
        val call = authTwitterService.likeTweet(idTweet)

        call.enqueue(object : Callback<Tweet>{
            override fun onResponse(call: Call<Tweet>, response: Response<Tweet>) {
                if(response.isSuccessful){
                    val listClonned = ArrayList<Tweet>()
                    listClonned.add(response.body()!!)

                    allTweets.value?.forEach {tweet: Tweet ->
                        val tweetToInsertOnList =
                                if(tweet.id == idTweet){
                                    response.body()
                                } else {
                                    Tweet(tweet)
                                }

                        listClonned.add(tweetToInsertOnList!!)
                    }

                    allTweets.value = listClonned
                    getFavTweets()
                } else {
                    // TODO: Refactor to include the error message on string
                    Toast.makeText(MyApp.context, "Something goes wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Tweet>, t: Throwable) {
                // TODO: Refactor to include the error message on string
                Toast.makeText(MyApp.context, "Error on connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deleteTweet(idTweet: Int){
        val call = authTwitterService.deleteTweet(idTweet)

        call.enqueue(object : Callback<TweetDeleted>{
            override fun onResponse(call: Call<TweetDeleted>, response: Response<TweetDeleted>) {
                if(response.isSuccessful){
                    val clonnedTweets = ArrayList<Tweet>()

                    allTweets.value?.forEach { tweet: Tweet ->
                        if(tweet.id != idTweet){
                            clonnedTweets.add(Tweet(tweet))
                        }
                    }

                    allTweets.value = clonnedTweets
                    getFavTweets()
                } else {
                    // TODO: Refactor to include the error message on string
                    Toast.makeText(MyApp.context, "Something goes wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TweetDeleted>, t: Throwable) {
                // TODO: Refactor to include the error message on string
                Toast.makeText(MyApp.context, "Error on connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

}