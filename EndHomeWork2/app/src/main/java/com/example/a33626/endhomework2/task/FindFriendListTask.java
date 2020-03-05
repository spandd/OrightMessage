package com.example.a33626.endhomework2.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.Friend;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;

import java.io.Serializable;
import java.util.List;

public class FindFriendListTask implements Runnable {
    private Integer userId;
    private Handler friendHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public FindFriendListTask(Handler friendHandler) {
        this.friendHandler = friendHandler;
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.get(Constants.BASE_URL + "/friend/findFriendList?userId=" + this.userId);
        if (resultInfo == null || resultInfo.equals("")){
            return;
        }
        List<Friend> friends = (List<Friend>)JsonUtil.jsonArrayToObjectList(resultInfo,Friend.class);
        Message message = new Message();
        message.what = Constants.FIND_FRIEND_LIST;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.FRIEND_INFO_LIST,(Serializable) friends);
        message.setData(bundle);
        this.friendHandler.sendMessage(message);
    }

    public void init(Integer userId){
        this.userId = userId;
    }
}
