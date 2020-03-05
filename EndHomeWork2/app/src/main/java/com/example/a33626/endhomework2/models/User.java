package com.example.a33626.endhomework2.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String userName;
    private String password;
    private String birthdayMonth;
    private String birthdayDay;
    private String stringHeadPortrait;
    private byte[] blobHeadPortrait;

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public String getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(String birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String getStringHeadPortrait() {
        return stringHeadPortrait;
    }

    public void setStringHeadPortrait(String stringHeadPortrait) {
        this.stringHeadPortrait = stringHeadPortrait;
    }

    public byte[] getBlobHeadPortrait() {
        return blobHeadPortrait;
    }

    public void setBlobHeadPortrait(byte[] blobHeadPortrait) {
        this.blobHeadPortrait = blobHeadPortrait;
    }
}
