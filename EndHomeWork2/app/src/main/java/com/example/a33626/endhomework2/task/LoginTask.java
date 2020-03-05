package com.example.a33626.endhomework2.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.BaseInfo;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;

public class LoginTask implements Runnable {
    private User user;
    private Handler loginHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public LoginTask(Handler loginHandler) {
        this.loginHandler = loginHandler;
        this.user = new User();
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.post(Constants.BASE_URL + "/base/login",JsonUtil.objectToJson(this.user,User.class));
        BaseInfo baseInfo = (BaseInfo)JsonUtil.jsonToObject(resultInfo,BaseInfo.class);
        Message message = new Message();
        message.what = baseInfo.getLoginStatus();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BASE_INFO,baseInfo);
        message.setData(bundle);
        this.loginHandler.sendMessage(message);
    }

    public void init(String userName,String password){
        this.user.setUserName(userName);
        this.user.setPassword(password);
    }

}
