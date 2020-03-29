package com.lux.eventmanagement.fragments;

import java.io.Serializable;

public class ProfileUserData implements Serializable {
    public String id;
    public String address;
    public String name;
    public String aboutme;
    public String phonenumber;
    public String email;

    public ProfileUserData() {
    }

    public ProfileUserData(String id, String address, String name, String aboutme,  String phonenumber, String email) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.aboutme = aboutme;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
