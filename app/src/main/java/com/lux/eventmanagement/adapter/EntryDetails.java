package com.lux.eventmanagement.adapter;

import com.lux.eventmanagement.fragments.ProfileUserData;

import java.io.Serializable;


public class EntryDetails implements Serializable {

    String  description;
    String  image;
    String  title;
    String  id;
    ProfileUserData user;
    double longitude ;
    double latitude ;


    public EntryDetails() {

    }

    public EntryDetails(String id,String description,  String image, String title,double longitude ,
            double latitude , ProfileUserData user) {
        this.description = description;
        this.image = image;
        this.title = title;
        this.user = user;
        this.id = id;
        this. longitude = longitude ;
        this. latitude = latitude;
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
}
