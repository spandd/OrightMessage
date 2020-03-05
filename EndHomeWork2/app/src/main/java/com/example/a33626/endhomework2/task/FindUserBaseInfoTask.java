package com.example.a33626.endhomework2.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;

public class FindUserBaseInfoTask implements Runnable {
    private User user;
    private Handler mainHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public FindUserBaseInfoTask(Handler mainHandler) {
        this.mainHandler = mainHandler;
        this.user = new User();
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.get(Constants.BASE_URL + "/base/findUserBaseInfo?userName=" + this.user.getUserName());
        User resUser  = (User) JsonUtil.jsonToObject(resultInfo,User.class);
        Message message = new Message();
        message.what = Constants.FIND_USER_BASE_INFO;
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.USER_INFO,resUser);
        message.setData(bundle);
        this.mainHandler.sendMessage(message);
    }

    public void init(String userName){
        this.user.setUserName(userName);
    }
}
