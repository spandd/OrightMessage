package com.example.a33626.endhomework2.task;

import android.os.Handler;
import android.os.Message;

import com.example.a33626.endhomework2.constants.Constants;
import com.example.a33626.endhomework2.models.User;
import com.example.a33626.endhomework2.utils.HttpUrlConnectionUtil;
import com.example.a33626.endhomework2.utils.JsonUtil;

public class EditHeadPortraitTask implements Runnable {
    private User user;
    private Handler headPortraitHandler;
    private HttpUrlConnectionUtil httpUrlConnectionUtil;
    public EditHeadPortraitTask(Handler headPortraitHandler) {
        this.headPortraitHandler = headPortraitHandler;
        this.httpUrlConnectionUtil = new HttpUrlConnectionUtil();
    }

    @Override
    public void run() {
        String resultInfo = this.httpUrlConnectionUtil.post(Constants.BASE_URL + "/square/editUserHeadPortrait",JsonUtil.objectToJson(this.user,User.class));
        if (resultInfo.equals("1")){
            Message message = new Message();
            message.what = Constants.EDIT_USER_HEAD_PORTRAIT;
            this.headPortraitHandler.sendMessage(message);
        }
        else{
            return;
        }
    }

    public void init(User user){
        this.user = user;
    }
}
