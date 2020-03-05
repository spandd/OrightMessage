package com.example.a33626.endhomework2.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.BaseInfo;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;



public class RegisterTask implements Runnable {
    private User user;
    private Handler registerHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public RegisterTask(Handler registerHandler) {
        this.registerHandler = registerHandler;
        this.user = new User();
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.post(Constants.BASE_URL + "/base/register",JsonUtil.objectToJson(this.user,User.class));
        BaseInfo baseInfo = (BaseInfo)JsonUtil.jsonToObject(resultInfo,BaseInfo.class);
        Message message = new Message();
        message.what = baseInfo.getRegisterStatus();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BASE_INFO,baseInfo);
        bundle.putSerializable(Constants.USER_INFO,this.user);
        message.setData(bundle);
        this.registerHandler.sendMessage(message);
    }

    public void init(String userName,String password,String birthdayMonth,String birthdayDay){
        this.user.setUserName(userName);
        this.user.setPassword(password);
        this.user.setBirthdayMonth(birthdayMonth);
        this.user.setBirthdayDay(birthdayDay);
    }

}
