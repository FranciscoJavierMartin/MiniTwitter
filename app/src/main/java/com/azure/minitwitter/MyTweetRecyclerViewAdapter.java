package com.azure.minitwitter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.azure.minitwitter.common.Constants;
import com.azure.minitwitter.common.SharedPreferencesManager;

import com.azure.minitwitter.retrofit.response.Like;
import com.azure.minitwitter.retrofit.response.Tweet;
import com.bumptech.glide.Glide;

import java.util.List;


public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {

    private Context ctx;
    private List<Tweet> mValues;
    String username;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        mValues = items;
        ctx = context;
        username = SharedPreferencesManager.getSomeStringValue(Constants.PREF_USERNAME);
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

            holder.tvUsername.setText(holder.mItem.getUser().getUsername());
            holder.tvMessage.setText(holder.mItem.getMensaje());
            holder.tvLikesCount.setText(String.valueOf(holder.mItem.getLikes().size()));

            String photoUrl = holder.mItem.getUser().getPhotoUrl();

            if(!holder.mItem.getUser().getPhotoUrl().equals("")){
                Glide.with(ctx)
                        .load("https//www.minitwitter.com/apiv1/uploads/photos/"+photoUrl)
                        .into(holder.ivAvatar);
            }

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
        public final TextView tvUsername;
        public final TextView tvMessage;
        public final TextView tvLikesCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ivAvatar = view.findViewById(R.id.imageViewAvatar);
            ivLike = view.findViewById(R.id.imageViewLike);
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
