package com.lux.eventmanagement.Entities;

public class Profile {
    private String uid;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String type;
    private String about;

    public Profile(){}

    public Profile(String name, String email, String address, String phone, String type, String about) {

        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.type = type;
        this.about = about;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
