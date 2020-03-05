package com.example.a33626.endhomework2.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.BaseInfo;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;

public class RegisterFindUserNameTask implements Runnable{
    private User user;
    private Handler registerHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public RegisterFindUserNameTask(Handler registerHandler) {
        this.registerHandler = registerHandler;
        this.user = new User();
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.get(Constants.BASE_URL + "/base/isRepeat?userName=" + this.user.getUserName());
        BaseInfo baseInfo = (BaseInfo)JsonUtil.jsonToObject(resultInfo,BaseInfo.class);
        Message message = new Message();
        message.what = baseInfo.getRegisterStatus();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BASE_INFO,baseInfo);
        message.setData(bundle);
        this.registerHandler.sendMessage(message);
    }

    public void init(String userName){
        this.user.setUserName(userName);
    }

}
