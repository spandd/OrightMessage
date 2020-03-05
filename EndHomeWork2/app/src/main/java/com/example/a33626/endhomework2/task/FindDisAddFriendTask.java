package com.example.a33626.endhomework2.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;

public class FindDisAddFriendTask implements Runnable{
    private Integer userId;
    private Integer friendId;
    private Handler friendHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public FindDisAddFriendTask(Handler friendHandler) {
        this.friendHandler = friendHandler;
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.get(Constants.BASE_URL + "/friend/findDisAddFriend?userId=" + this.userId + "&friendId=" + this.friendId);
        Message message = new Message();
        message.what = Constants.FIND_DIS_ADD_FRIEND;
        Bundle bundle = new Bundle();
        if (resultInfo.equals("")){
            bundle.putSerializable(Constants.USER_INFO,null);
        }
        else {
            bundle.putSerializable(Constants.USER_INFO,(User)JsonUtil.jsonToObject(resultInfo,User.class));
        }

        message.setData(bundle);
        this.friendHandler.sendMessage(message);
    }

    public void init(Integer userId,Integer friendId){
        this.userId = userId;
        this.friendId = friendId;
    }
}
