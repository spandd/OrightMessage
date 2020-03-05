package com.example.a33626.endhomework2.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;

import java.io.Serializable;

/**
 * 这个是接收器
 */
public class MessageReceiver extends BroadcastReceiver {

    private Handler handler;

    public MessageReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //当接收到广播以后 会给主线程中的handler发送消息  然后更新UI
        Message message = new Message();
        message.what = Constants.RECEIVE_MESSAGE;
        //当然 message中用 bundle传送数据
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CHAT_INFO,(Serializable)intent.getSerializableExtra(Constants.CHAT_INFO));
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
