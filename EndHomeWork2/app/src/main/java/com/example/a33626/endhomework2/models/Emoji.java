package com.example.a33626.endhomework2.models;

import java.io.Serializable;

public class Emoji implements Serializable {

    private String emojiName;
    private Integer emojiId;

    public String getEmojiName() {
        return emojiName;
    }

    public void setEmojiName(String emojiName) {
        this.emojiName = emojiName;
    }

    public Integer getEmojiId() {
        return emojiId;
    }

    public void setEmojiId(Integer emojiId) {
        this.emojiId = emojiId;
    }
}
