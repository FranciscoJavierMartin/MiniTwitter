package com.azure.minitwitter.retrofit.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestUserProfile {

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("password")
    @Expose
    private String password;

    public RequestUserProfile() {
    }

    public RequestUserProfile(String username, String email, String description, String website, String password) {
        this.username = username;
        this.email = email;
        this.description = description;
        this.website = website;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
