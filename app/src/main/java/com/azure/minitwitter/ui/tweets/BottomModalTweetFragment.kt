package com.azure.minitwitter.ui.tweets

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.azure.minitwitter.R
import com.azure.minitwitter.common.Constants
import com.azure.minitwitter.data.TweetViewModel
import kotlinx.android.synthetic.main.bottom_modal_tweet_fragment.*

class BottomModalTweetFragment: BottomSheetDialogFragment(){

    private var idTweetToDelete: Int = -1
    private lateinit var tweetViewModel: TweetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments != null){
            idTweetToDelete = arguments!!.getInt(Constants.ARG_TWEET_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false)

        navigation_view_bottom_tweet.setNavigationItemSelectedListener { menuItem: MenuItem ->
            val id = menuItem.itemId
            var res: Boolean

            when(id){
                R.id.action_delete_tweet -> {
                    tweetViewModel.deleteTweet(idTweetToDelete)
                    dialog.dismiss()
                    res = true
                }
                else -> res = false

            }

            return res
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tweetViewModel = ViewModelProviders.of(activity!!).get(TweetViewModel::class.java)
    }

}