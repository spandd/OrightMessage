package com.example.a33626.endhomework2.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class MessageInfo implements Serializable {
    private Bitmap friendHeadPortrait;
    private String friendName;
    private String chatContent;
    private Integer friendId;
    private int chatType;

    public Bitmap getFriendHeadPortrait() {
        return friendHeadPortrait;
    }

    public void setFriendHeadPortrait(Bitmap friendHeadPortrait) {
        this.friendHeadPortrait = friendHeadPortrait;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }
}
