package com.example.a33626.endhomework2.models;

import android.graphics.Bitmap;

import java.io.Serializable;


public class Friend implements Serializable {

    private Integer userId;
    private Integer friendId;
    private String friendName;
    private String friendBirthdayMonth;
    private String friendBirthdayDay;
    private Bitmap friendHeadPortrait;
    private String remarks;
    private Integer meIsAccept;
    private Integer friendIsAccept;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendBirthdayMonth() {
        return friendBirthdayMonth;
    }

    public void setFriendBirthdayMonth(String friendBirthdayMonth) {
        this.friendBirthdayMonth = friendBirthdayMonth;
    }

    public String getFriendBirthdayDay() {
        return friendBirthdayDay;
    }

    public void setFriendBirthdayDay(String friendBirthdayDay) {
        this.friendBirthdayDay = friendBirthdayDay;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getMeIsAccept() {
        return meIsAccept;
    }

    public void setMeIsAccept(Integer meIsAccept) {
        this.meIsAccept = meIsAccept;
    }

    public Integer getFriendIsAccept() {
        return friendIsAccept;
    }

    public void setFriendIsAccept(Integer friendIsAccept) {
        this.friendIsAccept = friendIsAccept;
    }


    public Bitmap getFriendHeadPortrait() {
        return friendHeadPortrait;
    }

    public void setFriendHeadPortrait(Bitmap friendHeadPortrait) {
        this.friendHeadPortrait = friendHeadPortrait;
    }
}
