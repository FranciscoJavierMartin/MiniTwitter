package com.azure.minitwitter.ui.tweets;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azure.minitwitter.R;
import com.azure.minitwitter.common.Constants;
import com.azure.minitwitter.common.SharedPreferencesManager;

import com.azure.minitwitter.data.TweetViewModel;
import com.azure.minitwitter.retrofit.response.Like;
import com.azure.minitwitter.retrofit.response.Tweet;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;


public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context ctx;
    private List<Tweet> mValues;
    String username;
    TweetViewModel tweetViewModel;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        mValues = items;
        ctx = context;
        username = SharedPreferencesManager.getSomeStringValue(Constants.PREF_USERNAME);
        tweetViewModel = ViewModelProviders.of((FragmentActivity)ctx).get(TweetViewModel.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(mValues != null){
            holder.mItem = mValues.get(position);

            holder.tvUsername.setText("@" + holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMessage());
            holder.tvLikesCount.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photoUrl = holder.mItem.getUser().getPhotoUrl();

            if(!photoUrl.equals("")){
                Glide.with(ctx)
                        .load(Constants.API_MINITWITTER_FILES_URL+photoUrl)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(holder.ivAvatar);
            }

            Glide.with(ctx)
                    .load(R.drawable.ic_like)
                    .into(holder.ivLike);
            holder.tvLikesCount.setTextColor(ctx.getResources().getColor(android.R.color.black));
            holder.tvLikesCount.setTypeface(null, Typeface.NORMAL);

            holder.ivLike.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    tweetViewModel.likeTweet(holder.mItem.getId());
                }
            });

            if(holder.mItem.getUser().getUsername().equals(username)){
                holder.ivShowMenu.setVisibility(View.VISIBLE);
            } else {
                holder.ivShowMenu.setVisibility(View.GONE);
            }

            holder.ivShowMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tweetViewModel.openDialogTweetMenu(ctx, holder.mItem.getId());
                }
            });

            for(Like like: holder.mItem.getLikes()){
                if(like.getUsername().equals(username)){
                    Glide.with(ctx)
                            .load(R.drawable.ic_like_pink)
                            .into(holder.ivLike);
                    holder.tvLikesCount.setTextColor(ctx.getResources().getColor(R.color.pink));
                    holder.tvLikesCount.setTypeface(null, Typeface.BOLD);
                }
                break;
            }
        }
    }

    public void setData(List<Tweet> tweetList){
        mValues = tweetList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mValues!=null ? mValues.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView ivAvatar;
        public final ImageView ivLike;
        public final ImageView ivShowMenu;
        public final TextView tvUsername;
        public final TextView tvMessage;
        public final TextView tvLikesCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAvatar = view.findViewById(R.id.imageViewAvatar);
            ivLike = view.findViewById(R.id.imageViewLike);
            ivShowMenu = view.findViewById(R.id.imageViewShowMenu);
            tvUsername = view.findViewById(R.id.textViewUsername);
            tvMessage = view.findViewById(R.id.textViewMessage);
            tvLikesCount = view.findViewById(R.id.textViewLiesCount);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvUsername.getText() + "'";
        }
    }
}
