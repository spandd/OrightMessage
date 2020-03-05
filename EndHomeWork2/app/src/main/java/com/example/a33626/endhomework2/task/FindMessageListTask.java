package com.example.a33626.endhomework2.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.MessageInfo;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;

import java.io.Serializable;
import java.util.List;

public class FindMessageListTask implements Runnable{

    private Integer userId;
    private Handler messageHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public FindMessageListTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.get(Constants.BASE_URL + "/message/findMessageList?userId=" + this.userId);
        if (resultInfo == null || resultInfo.equals("")){
            return;
        }
        List<MessageInfo> messageInfos = (List<MessageInfo>)JsonUtil.jsonArrayToObjectList(resultInfo,MessageInfo.class);
        Message message = new Message();
        message.what = Constants.FIND_MESSAGE_LIST;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.MESSAGE_INFO_LIST,(Serializable) messageInfos);
        message.setData(bundle);
        this.messageHandler.sendMessage(message);
    }

    public void init(Integer userId){
        this.userId = userId;
    }
}
