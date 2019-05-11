package com.azure.minitwitter.ui.tweets

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.azure.minitwitter.R
import kotlinx.android.synthetic.main.fragment_tweet.view.*

class MyTweetRecyclerViewAdapter : RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder>(){


}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

    val textViewUsername = view.findViewById<TextView>(R.id.textViewUsername)

    init {

    }
    override fun toString(): String =
            super.toString()+ " '"+textViewUsername.text.toString()+"'"

}