package com.lux.eventmanagement.adapter;

import com.lux.eventmanagement.fragments.ProfileUserData;

import java.io.Serializable;
import java.util.List;


public class EntryDetails implements Serializable {

    String  description;
    String  image;
    String  video;
    String  title;
    String  id;
    ProfileUserData user;
    double longitude ;
    double latitude ;
    List<UserCommentList> mUserCommentList ;
    String rate;

    public EntryDetails() {

    }

    public EntryDetails(String id,String description,  String image, String title,double longitude ,
            double latitude , ProfileUserData user, List<UserCommentList> mUserCommentList , String rate,String video) {
        this.description = description;
        this.image = image;
        this.title = title;
        this.user = user;
        this.id = id;
        this. longitude = longitude ;
        this. latitude = latitude;
        this. mUserCommentList = mUserCommentList;
        this. rate = rate;
        this. video = video;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProfileUserData getUser() {
        return user;
    }

    public void setUser(ProfileUserData user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public  List<UserCommentList> getUserCommentList() {
        return mUserCommentList;
    }

    public void setUserCommentList( List<UserCommentList> mUserCommentList) {
        this.mUserCommentList = mUserCommentList;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
