package com.azure.minitwitter.ui.tweets;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.azure.minitwitter.R;
import com.azure.minitwitter.common.Constants;
import com.azure.minitwitter.common.SharedPreferencesManager;
import com.azure.minitwitter.data.TweetViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class NewTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageView ivClose, ivAvatar;
    Button btnTweet;
    EditText etMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.new_tweet_full_dialog, container, false);

        ivClose = view.findViewById(R.id.imageViewClose);
        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        btnTweet = view.findViewById(R.id.buttonTweet);
        etMessage = view.findViewById(R.id.editTextMessage);

        btnTweet.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constants.PREF_PHOTOURL);

        if(!photoUrl.isEmpty()){
            Glide.with(getActivity())
                    .load(Constants.API_MINITWITTER_FILES_URL + photoUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(ivAvatar);
        }



        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String message = etMessage.getText().toString();

        switch (id){
            case R.id.imageViewClose:
                if(!message.isEmpty()){
                    showDialogConfirm();
                } else{
                    getDialog().dismiss();
                }
                break;
            case R.id.buttonTweet:
                if(message != null && !message.isEmpty()){
                    TweetViewModel tweetViewModel = ViewModelProviders.of(getActivity())
                            .get(TweetViewModel.class);
                    tweetViewModel.insertTweet(message);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getActivity(),
                            getResources().getText(R.string.new_tweet_dialog_write_a_message),
                            Toast.LENGTH_SHORT);
                }

                break;
            default:
        }
    }

    private void showDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getResources().getString(R.string.new_tweet_dialog_cancel_tweet_body))
                .setTitle(getResources().getString(R.string.new_tweet_dialog_cancel_tweet_header));

        builder.setPositiveButton(R.string.new_tweet_dialog_cancel_tweet_positive_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getDialog().dismiss();
            }
        });

        builder.setNegativeButton(R.string.new_tweet_dialog_cancel_tweet_negative_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
