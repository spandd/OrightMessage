package com.example.a33626.endhomework2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;

import java.io.Serializable;

public class FriendReceiver extends BroadcastReceiver {
    private Handler handler;

    public FriendReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Message message = new Message();
        message.what = Constants.RECEIVE_FRIEND;
        handler.sendMessage(message);
    }
}
