package com.lux.eventmanagement.adapter;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 */


public class UserLocalDataList {

    @SerializedName("authorName")
    public String authorName;
    @SerializedName("authorId")
    public Integer authorId;

    @SerializedName("data")
    public List<EntryDetails> data = new ArrayList<>();


    public List<EntryDetails> getData() {
        return data;
    }

    public void setData(List<EntryDetails> data) {
        this.data = data;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }
}
