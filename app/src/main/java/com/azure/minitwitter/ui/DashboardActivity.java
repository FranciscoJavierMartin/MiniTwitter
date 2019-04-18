package com.azure.minitwitter.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.azure.minitwitter.R;
import com.azure.minitwitter.common.Constants;
import com.azure.minitwitter.common.SharedPreferencesManager;
import com.bumptech.glide.Glide;

public class DashboardActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ImageView ivAvatar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean changeTab = false;
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_ALL);
                    fab.show();
                    break;
                case R.id.navigation_tweets_like:
                    fragment = TweetListFragment.newInstance(Constants.TWEET_LIST_FAVS);
                    fab.hide();
                    break;
                case R.id.navigation_profile:

                    fab.hide();
                    break;
            }

            if(fragment != null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .commit();
                changeTab = true;
            }

            return changeTab;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fab = findViewById(R.id.fab);
        ivAvatar = findViewById(R.id.imageViewToolbarPhoto);

        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, TweetListFragment.newInstance(Constants.TWEET_LIST_ALL))
                .commit();

        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NewTweetDialogFragment dialog = new NewTweetDialogFragment();
                dialog.show(getSupportFragmentManager(), "NewTweetDialogFragment");
            }
        });

        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constants.PREF_PHOTOURL);

        if(!photoUrl.isEmpty()){
            Glide.with(this)
                    .load(Constants.API_MINITWITTER_FILES_URL + photoUrl)
                    .into(ivAvatar);
        }
    }

}
