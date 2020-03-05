package com.example.a33626.endhomework2.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Chat implements Serializable {

    private Integer userId;
    private Integer friendId;
    private String chatContent;
    private Bitmap chatPhotoB;
    private String chatPhotoS;
    private String chatLatitude;
    private String chatLongitude;
    private String chatDirection;
    private String chatAccuracy;
    private Integer isLast;
    private Integer isRead;
    private Integer isMe;
    private int chatType;

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

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public Bitmap getChatPhotoB() {
        return chatPhotoB;
    }

    public void setChatPhotoB(Bitmap chatPhotoB) {
        this.chatPhotoB = chatPhotoB;
    }

    public String getChatPhotoS() {
        return chatPhotoS;
    }

    public void setChatPhotoS(String chatPhotoS) {
        this.chatPhotoS = chatPhotoS;
    }

    public String getChatLatitude() {
        return chatLatitude;
    }

    public void setChatLatitude(String chatLatitude) {
        this.chatLatitude = chatLatitude;
    }

    public String getChatLongitude() {
        return chatLongitude;
    }

    public void setChatLongitude(String chatLongitude) {
        this.chatLongitude = chatLongitude;
    }

    public String getChatDirection() {
        return chatDirection;
    }

    public void setChatDirection(String chatDirection) {
        this.chatDirection = chatDirection;
    }

    public String getChatAccuracy() {
        return chatAccuracy;
    }

    public void setChatAccuracy(String chatAccuracy) {
        this.chatAccuracy = chatAccuracy;
    }

    public Integer getIsLast() {
        return isLast;
    }

    public void setIsLast(Integer isLast) {
        this.isLast = isLast;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getIsMe() {
        return isMe;
    }

    public void setIsMe(Integer isMe) {
        this.isMe = isMe;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
}
