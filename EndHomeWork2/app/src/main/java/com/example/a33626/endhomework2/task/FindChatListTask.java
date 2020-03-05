package com.example.a33626.endhomework2.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Chat;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;
import java.io.Serializable;
import java.util.List;

public class FindChatListTask implements Runnable {
    private Friend friend;
    private Handler chatHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public FindChatListTask(Handler chatHandler) {
        this.chatHandler = chatHandler;
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.post(Constants.BASE_URL + "/chat/findChatList",JsonUtil.objectToJson(friend,Friend.class));
        Message message = new Message();
        message.what = Constants.FIND_CHAT_LIST;
        Bundle bundle = new Bundle();
        if (resultInfo.equals("") || resultInfo == null){
            return;
        }
        else {
            List<Chat> chats = (List<Chat>) JsonUtil.jsonArrayToObjectList(resultInfo,Chat.class);
            bundle.putSerializable(Constants.CHAT_INFO_LIST,(Serializable) chats);
        }
        message.setData(bundle);
        this.chatHandler.sendMessage(message);
    }

    public void init(Friend friend){
        this.friend = friend;
    }
}
