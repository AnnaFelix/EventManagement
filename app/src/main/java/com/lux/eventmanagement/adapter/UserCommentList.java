package com.lux.eventmanagement.adapter;


import com.lux.eventmanagement.fragments.ProfileUserData;

public class UserCommentList {

    //@SerializedName("name")
    public ProfileUserData user;
    //@SerializedName("job")
    public String eventID;
    public String usercomment;

    public UserCommentList(ProfileUserData user, String eventID, String usercomment) {
        this.user = user;
        this.eventID = eventID;
        this.usercomment = usercomment;
    }

    public UserCommentList() {
    }

    public ProfileUserData getUser() {
        return user;
    }

    public void setUser(ProfileUserData user) {
        this.user = user;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUsercomment() {
        return usercomment;
    }

    public void setUsercomment(String usercomment) {
        this.usercomment = usercomment;
    }
}
