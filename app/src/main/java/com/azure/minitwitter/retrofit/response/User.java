
package com.azure.minitwitter.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("photoUrl")
    @Expose
    private String photoUrl;
    @SerializedName("created")
    @Expose
    private String created;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param id
     * @param username
     * @param created
     * @param website
     * @param description
     * @param photoUrl
     */
    public User(Integer id, String username, String description, String website, String photoUrl, String created) {
        super();
        this.id = id;
        this.username = username;
        this.description = description;
        this.website = website;
        this.photoUrl = photoUrl;
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}