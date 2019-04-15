
package com.azure.minitwitter.retrofit.response;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tweet {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("likes")
    @Expose
    private List<Like> likes = new ArrayList<Like>();
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * No args constructor for use in serialization
     *
     */
    public Tweet() {
    }

    public Tweet(Tweet newTweet){
        this.id = newTweet.getId();
        this.message = newTweet.getMessage();
        this.likes = newTweet.getLikes();
        this.user = newTweet.getUser();
    }

    /**
     *
     * @param id
     * @param likes
     * @param message
     * @param user
     */
    public Tweet(Integer id, String message, List<Like> likes, User user) {
        super();
        this.id = id;
        this.message = message;
        this.likes = likes;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}